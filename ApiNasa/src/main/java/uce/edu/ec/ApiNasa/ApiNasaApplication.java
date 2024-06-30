package uce.edu.ec.ApiNasa;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.ApiResponse;
import models.Photo;
import view.PhotoViewer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ApiNasaApplication {

	public static void main(String[] args) {
		try {
			URL url = new URL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=K2TWC5BvuqR1f4Z8601cKSJC1b0Gdx4X7saU5dZE");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();

			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				// Parsear JSON a objetos
				Gson gson = new Gson();
				Type listType = new TypeToken<ApiResponse>(){}.getType();
				ApiResponse apiResponse = gson.fromJson(response.toString(), listType);

				// Crear lista inmutable
				List<Photo> photos = Collections.unmodifiableList(apiResponse.photos);

				// Mostrar resultados
				new PhotoViewer(photos).setVisible(true);

				connection.disconnect();
			} else {
				throw new RuntimeException("Error al conectarnos a la API: " + responseCode);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
