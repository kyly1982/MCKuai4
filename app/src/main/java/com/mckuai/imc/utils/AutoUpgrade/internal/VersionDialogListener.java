package com.mckuai.imc.utils.AutoUpgrade.internal;

public interface VersionDialogListener {
	void doUpdate(boolean laterOnWifi);
	void doIgnore();
}
