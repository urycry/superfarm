package com.richardhughes.superfarm;

public class Tokenizer {

	public String Tokenize(String s, SuperFarmGame game) {

		// date
		s = s.replace("##d##", ((Integer)game.GetGameTime().GetDayOfMonth()).toString());
		s = s.replace("##m##", game.GetGameTime().GetMonth().toString());
		s = s.replace("##y##", ((Integer)game.GetGameTime().GetYear()).toString());
		s = s.replace("##day##", game.GetGameTime().GetDay().toString());

		// time
		s = s.replace("##h##", ((Integer)game.GetGameTime().GetHours()).toString());
		int hours = game.GetGameTime().GetHours();
		s = s.replace("##h24##", hours < 9 ? "0" + ((Integer)hours).toString() : ((Integer)hours).toString());
		s = s.replace("##M##", ((Integer)game.GetGameTime().GetMinutes()).toString());
		int minutes = game.GetGameTime().GetMinutes();
		s = s.replace("##M60##", minutes < 9 ? "0" + ((Integer)minutes).toString() : ((Integer)minutes).toString());
		s = s.replace("##s##", ((Integer)game.GetGameTime().GetSeconds()).toString());
		
		return s;
	}
}
