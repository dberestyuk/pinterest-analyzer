package com.lue.pintrests;

import static com.lue.pintrests.Analyzeservlet.filterkeyword;
import static com.lue.pintrests.Analyzeservlet.numberOfPinsToAnalyze;
import static com.lue.pintrests.Analyzeservlet.offsetNumberOfPins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lue.board.Board;
import com.lue.board.BoardSearchResponse;
import com.lue.pins.PinData;
import com.lue.pins.PinSearchResponse;
import com.lue.popularityScore.PinDetails;
import com.lue.popularityScore.PinResponse;

public class PinterestAnalyzer {

	public static String accessToken;

	private static final Logger log = LoggerFactory
			.getLogger(PinterestAnalyzer.class);

	static {
		InputStream input = null;
		try {
			input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("accessToken.properties");
			Properties properties = new Properties();
			properties.load(input);
			accessToken = properties.getProperty("accesstoken");
			log.info("Using token:{}", accessToken);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	public static Board getBoard(String boardName) {
		String output = null;
		Board b = null;
		try {
			URL url = new URL(
					"https://api.pinterest.com/v1/me/search/boards/?query="
							+ boardName + "&access_token=" + accessToken
							+ "&fields=id%2Cname%2Curl");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			conn.disconnect();

			JSONObject jso = new JSONObject(sb.toString());

			ObjectMapper mapper = new ObjectMapper()
					.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			BoardSearchResponse resp = mapper.readValue(jso.toString(),
					BoardSearchResponse.class);
			if (resp.getData().size() > 0) {
				b = resp.getData().get(0);
				log.info("Found: {}", b.toString());
			} else {
				throw new RuntimeException("No boards found with name: "
						+ boardName);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return b;
	}

	public static List<PinData> getPinsForBoard(String boardId) {
		List<PinData> dataBank = new ArrayList<PinData>();

		String newUrl = "https://api.pinterest.com/v1/boards/" + boardId
				+ "/pins/?access_token=" + accessToken
				+ "&fields=id%2Clink%2Cnote%2Curl";
		String output = null;
		try {
			
			int limit = Math.min(100, numberOfPinsToAnalyze);
			
			int numFetches = ((offsetNumberOfPins + numberOfPinsToAnalyze) / 100) + 1;
			for (int n = 0; n < numFetches; n++) {
				URL url = new URL(newUrl + "&limit="+limit);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				StringBuilder sb = new StringBuilder();
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				conn.disconnect();

				JSONObject jso = new JSONObject(sb.toString());

				ObjectMapper mapper = new ObjectMapper()
						.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
				PinSearchResponse resp = mapper.readValue(jso.toString(),
						PinSearchResponse.class);
				dataBank.addAll(resp.getData());

				newUrl = resp.getPage().getNext();
				continue;
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.info("Loaded: {} Pins", dataBank.size());
		return dataBank;
	}

	public static Map<String, FinalResult> analyzePins(List<PinData> dataBank) {
		Map<String, FinalResult> results = new HashMap<String, FinalResult>();

		for (PinData pin : dataBank) {
			
			// Analyze if matched on keywords
			if (pin.getNote().indexOf(filterkeyword) != -1) {
				FinalResult fr = new FinalResult();

				fr.setUrl(pin.getUrl());
				fr.setLink(pin.getLink());
				fr.setNote(pin.getNote());
				fr.setPinId(pin.getId());

				List<Object> resultList = getPinDetails(pin.getId());

				fr.setRepins((Double) resultList.get(0));
				fr.setLikes((Double) resultList.get(1));
				fr.setImageUrl(resultList.get(2).toString());

				results.put(pin.getId(), fr);
			}
		}
		
		return results;
	}

	private static List<Object> getPinDetails(String pinid) {
		String output = null;
		try {
			URL url = new URL("https://api.pinterest.com/v1/pins/" + pinid
					+ "/?access_token=" + accessToken
					+ "&fields=image%2Ccounts");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			conn.disconnect();

			JSONObject jso = new JSONObject(sb.toString());

			ObjectMapper mapper = new ObjectMapper()
					.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			PinResponse pinresp = mapper.readValue(jso.toString(),
					PinResponse.class);

			if (pinresp != null) {
				PinDetails resp = pinresp.getData();
				String imageurl = resp.getImage().getOriginal().getUrl();
				Double likes = Double.parseDouble(resp.getCounts().getLikes());
				Double repins = Double
						.parseDouble(resp.getCounts().getRepins());

				ArrayList<Object> results = new ArrayList<Object>(3);
				results.add(0, repins);
				results.add(1, likes);
				results.add(2, imageurl);
				
				log.info("Fetch: {}", resp.toString());
				return results;
			} else {
				throw new RuntimeException ("Unable to get Pin Details for " + pinid);
			}

		} catch (Exception e) {
			throw new RuntimeException (e);
		}
	}

}
