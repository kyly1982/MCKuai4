package com.mckuai.imc.utils.AutoUpgrade;


/**
 * 自定义显示最新版本信息
 * 
 * @author ilovedeals
 * 
 */
public interface Displayer {
	void showFoundLatestVersion(Version version);
	void showIsLatestVersion();
}