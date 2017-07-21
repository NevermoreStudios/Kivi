package rs.kockasystems.kivi;

import com.badlogic.gdx.Gdx;

public class Config
{
	public static boolean preTitle, enableParticles;
	public static float soundVolume;
	
	public static void init()
	{
		try
		{
			String name, value, file = Gdx.files.internal("kivi.conf").readString();
			boolean found;
			for(String line : file.split("\n"))
			{
				name = value = "";
				found = false;
				for(int i = 0; i < line.length(); ++i)
				{
					if(line.charAt(i) == ':') found = true;
					else if(found)value += line.charAt(i);
					else name += line.charAt(i);
				}
				switch(name)
				{
					case "volume":
						soundVolume = Float.valueOf(value);
						break;
					case "particles":
						enableParticles = (value.charAt(0) == '1');
						break;
					case "title":
						preTitle = (value.charAt(0) == '1');
						break;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Configuration file not found!");
			preTitle = true;
			soundVolume = 1f;
			enableParticles = true;
		}
		
		
	}
	
}
