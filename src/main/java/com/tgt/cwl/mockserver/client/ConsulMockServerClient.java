package com.tgt.cwl.mockserver.client;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.util.Map;

import org.ho.yaml.Yaml;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;

import com.tgt.cwl.entities.Services;
import com.tgt.cwl.util.ConversionUtil;

public class ConsulMockServerClient {
	public static void main(String[] args) throws IOException, InterruptedException {
		Services serviceDetailsList = Yaml.loadType(ConversionUtil.convertFileContentToString("yml/serviceDetails.yml"),
				Services.class);
		startClientServer(serviceDetailsList);
		Thread.sleep(5000);
		setUpMockServer(serviceDetailsList);
	}

	private static void startClientServer(Services serviceDetailsList) {
		for (Map<String, Object> serviceDetails : serviceDetailsList.getDetails()) {
			startClientAndServer((Integer) serviceDetails.get("servicePort"));
			System.out.println("Started mock server on port - - - - " + (Integer) serviceDetails.get("servicePort"));
		}
	}

	private static void setUpMockServer(Services serviceDetailsList) throws IOException {
		for (Map<String, Object> serviceDetails : serviceDetailsList.getDetails()) {
			new MockServerClient("127.0.0.1", (Integer) serviceDetails.get("servicePort"))
					.when(request().withMethod((String) serviceDetails.get("httpMethod"))
							.withPath((String) serviceDetails.get("requestPath")), Times.unlimited())
					.respond(response().withStatusCode(200)
							.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
							.withBody(ConversionUtil.convertFileContentToString(
									"json/" + ((String) serviceDetails.get("serviceName")) + ".json"))
							.withDelay(new Delay(MICROSECONDS, 1)));
		}
	}
}
