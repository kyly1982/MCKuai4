package com.mckuai.imc.utils.AutoUpgrade.internal;


import com.mckuai.imc.utils.AutoUpgrade.ResponseParser;
import com.mckuai.imc.utils.AutoUpgrade.Version;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SimpleJSONParser implements ResponseParser {

	@Override
	public Version parser(String response) {
		try {
			JSONTokener jsonParser = new JSONTokener(response);
			JSONObject json = (JSONObject) jsonParser.nextValue();
			Version version = null;
			if (json.has("state") && json.has("dataObject")) {
				JSONObject dataField = json.getJSONObject("dataObject");
				int code = dataField.getInt("code");
				String name = dataField.getString("name");
				String feature = dataField.getString("feature");
				String targetUrl = dataField.getString("targetUrl");
				version = new Version(code, name, feature, targetUrl);
			}
			return version;
		} catch (JSONException exp) {
			exp.printStackTrace();
			return null;
		}
	}

}
