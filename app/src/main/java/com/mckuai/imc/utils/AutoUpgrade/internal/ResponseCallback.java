package com.mckuai.imc.utils.AutoUpgrade.internal;

import com.mckuai.imc.utils.AutoUpgrade.Version;

public interface ResponseCallback {
	void onFoundLatestVersion(Version version);
	void onCurrentIsLatest();
}
