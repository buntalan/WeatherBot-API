/*****************************************************************************
   Author:        Bryan Untalan
   Course:        CS2336.501
   Instructor:	  Dr. Khan
   Date:          2/23/2019
   Assignment:    Semester Project 1
   Summary: 
   Interacting with the internet; two request APIs and
   a chat bot
   
*****************************************************************************/
import java.io.*;						// For input output
import java.net.*;						// For URL class
import java.util.*;						// Java Utilities lirbary
import java.util.stream.Collectors;		// For BufferReader class
import java.util.regex.*;				// ... might not be needed, but for regular expressions
import org.jibble.pircbot.*;			// For PIRC Bot class to be extended to my bot
import com.google.gson.*;				// For JSON handling, class from Google

import okhttp3.MediaType;				// Defining your HTTP request to let server know what your media type is
import okhttp3.OkHttpClient;			// For instantiating OkHttpClient
import okhttp3.Request;					// Handles Request, and following handles the body
import okhttp3.RequestBody;
import okhttp3.Response;				// Class that handles response from GET request

public class Semester_Project_1 
{
	public static void main(String[] args) throws Exception
	{	
		Scanner input = new Scanner(System.in);		// Scanner to get user input
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// DRIVER FOR APIS
		/*
		String userInput;			// String for user input
		String endpoint;			// String for endpoint
		String delims = "[ .?!]*";	// Delimiters to be taken out
		WeatherMan weather;			// WeatherMan instance to request weather
		InternetGameDataBase game;	// InternetGameDataBase instance to connect and make request a GET
		

		// Prompt user for city and/or zipcode
		System.out.println("Please enter a city name and/or zip code with country code(us, uk) seperated by a comma "
							+ "and no spaces, or just press enter for Dallas weather.");
		userInput = input.nextLine();
		
		// Take out any spaces or unneeded punctuation
		userInput = userInput.replaceAll(delims, "");
		
		
		// Instantiate WeatherMan class with default constructor
		// unless given no input
		if(userInput.equals(""))
		{
			weather = new WeatherMan();
		}
		else
		{
			weather = new WeatherMan(userInput);
		}
		
		// Connect to API and buffer contents
		weather.connect();
		weather.buffer(weather.getHttpURLConnection());
		weather.convertBufferedReader(weather.getBufferedReader());
		System.out.println(WeatherMan.getTemperature(weather.getResult()));
		*/
		
		
		/*
		// Prompt user to inquire about a game
		System.out.println("Would you like to know about one game or many?");
		userInput = input.nextLine();
		
		// Instantiate IGDB class
		game = new InternetGameDataBase();
		game.connect();
		System.out.println(game.getSingleGameName(game.getJsonData()));
		*/
		// END OF DRIVER
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Create bot instance and start bot
		MyBot bot = new MyBot();
		
		// Enable debugging output
		bot.setVerbose(true);
		
		// Connect to the IRC server
		bot.connect("irc.freenode.net");
		
		// Join the #pircbot channel
		bot.joinChannel("#testChannel3");
		
		// Send a message to the IRC Channel
		bot.sendMessage("#testChannel3", "Hey! Enter any message and I'll respond");
		
		// Close input object
		input.close();
	}

} // end Semester_Project_1

// Weather API
class WeatherMan
{
	// Encapsulated data
	private String url;				// Concatenated URL
	private String apiURL;			// API URL
	private String input;			// User input (probably for city with or without country code)
	private String apiToken;		// ID for API
	private String result;			// Result of String conversion of rd (BufferedReader)
	private URL connect;					// URL object for connecting
	private HttpURLConnection conn;			// HttpURLConnection for opening connection
	private BufferedReader rd;				// BufferedReader to recieve incoming byte stream
	
	
	// Constructors
	// Default Constructor to grab Dallas weather
	public WeatherMan()
	{
		// Default parameters to call Dallas weather
		setAPIurl("http://api.openweathermap.org/data/2.5/weather?q=");
		setInput("Dallas");
		setAPIToken("&appid=e20fc1cd996241923c82ed943e0b55d8");
		setURL(getAPIurl() + getInput() + getAPIToken());
	}
	
	// Nondefault Constructor
	public WeatherMan(String input)
	{
		input = input.replaceAll("[ .?!]", "");
		setAPIurl("http://api.openweathermap.org/data/2.5/weather?q=");
		setInput(input);
		setAPIToken("&appid=e20fc1cd996241923c82ed943e0b55d8");
		setURL(getAPIurl() + getInput() + getAPIToken());
	}
	
	// Accessors
	// Get weather URL
	public String getURL()
	{
		return url;
	}
	
	// Get API URL
	public String getAPIurl()
	{
		return apiURL;
	}
	
	// Get user's input
	public String getInput()
	{
		return input;
	}
	
	// Get API Token
	public String getAPIToken()
	{
		return apiToken;
	}
	
	// Return URL object
	public URL getObjectURL()
	{
		return this.connect;
	}
	
	// Return HttpURLConnection conn
	public HttpURLConnection getHttpURLConnection()
	{
		return this.conn;
	}
	
	// Get BufferedReader
	public BufferedReader getBufferedReader()
	{
		return rd;
	}
	
	// Get result of BufferedReader conversion to String
	public String getResult()
	{
		return result;
	}
	
	
	// Mutators
	// Set weather URL
	public void setURL(String url)
	{
		this.url = url;
	}
	
	// Set API URL
	public void setAPIurl(String url)
	{
		this.apiURL = null;
		this.apiURL = new String(url);
	}
	
	// Set input
	public void setInput(String input)
	{
		this.input = null;
		this.input = new String(input);
	}
	
	// Set API token
	public void setAPIToken(String token)
	{
		this.apiToken = null;
		this.apiToken = new String(token);
	}
	
	// Set object URL
	public void setObjectURL(URL url)
	{
		this.connect = url;
	}
	
	// Set HttpURLConnection
	public void setHttpURLConnection(HttpURLConnection conn)
	{
		this.conn = conn;
	}
	
	// Set BufferedReader
	public void setBufferedReader(BufferedReader rd)
	{
		this.rd = rd;
	}
	
	// Set result String
	public void setResult(String result)
	{
		this.result = result;
	}
	
	
	// Methods
	// Connect to API and request
	public void connect()
	{
		// Try catch block to handle MalformedURLExceptions
		// and bad IO connections
		try 
		{
			setObjectURL(new URL(getURL()));
			conn = (HttpURLConnection) connect.openConnection();
			conn.setRequestMethod("GET");
		} 
		catch (MalformedURLException e) 
		{						
			// Notify user that URL is invalid
			System.out.println("URL is invalid");
			e.printStackTrace();
		}
		catch (IOException e) 
		{		
			// Notify user that url was not opened
			System.out.println("URL was not opened");
			e.printStackTrace();
		}
	}
	
	// Buffer input stream from request
	public void buffer(HttpURLConnection conn)
	{
		try 
		{
			setBufferedReader(new BufferedReader(new InputStreamReader(conn.getInputStream())));
		}
		catch (IOException e) 
		{		
			// Notify user that url was not opened
			System.out.println("URL was not opened");
			e.printStackTrace();
		}
	}
	
	// Convert request to string
	public void convertBufferedReader(BufferedReader rd)
	{
		setResult(rd.lines().collect(Collectors.joining(System.lineSeparator())));
	}
	
	// Parse Methods
	// Get temperature
	public String getTemperature(String json)
	{
		// Open a JsonObject with the non parsed string 
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		
		// Create a JsonObject with the main object from string
		JsonObject main = object.getAsJsonObject("main");
		
		// Create a String object to return and initialize with
		// temp variable from main object
		String cityName = object.get("name").getAsString();
		double temp = main.get("temp").getAsDouble();
		
		return "Current Temperature in " + cityName + ": " + temp;
	}
	
	// Get weather
	public String getWeather(String json)
	{
		// Open JsonObject for unparsed string
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		
		// Open weather JsonObject
		JsonArray weather = object.get("weather").getAsJsonArray();
		
		// Get 'name', 'main', 'description' attributes from weather object
		String cityName = object.get("name").getAsString();
		String main = ((JsonObject)weather.get(0)).get("main").getAsString();
		String description = ((JsonObject)weather.get(0)).get("description").getAsString();
		
		return "Weather in " + cityName + ": " + main + "\nDescription: " + description;
	}
	
	// Get humidity
	public String getHumidity(String json)
	{
		// Open Json Object to be parsed
		JsonObject object = new JsonParser().parse(json).getAsJsonObject();
		
		// Open main object from JSon
		JsonObject main = object.get("main").getAsJsonObject();
		
		// Get humidity and return as double
		String cityName = object.get("name").getAsString();
		double humidity = main.get("humidity").getAsDouble();
		
		return cityName + " humidity: " + humidity;
	}
}

// InternetGameDatabase API
class InternetGameDataBase
{
	// Encapsulated data
	private String apiURL;			// API URL
	private String endpoint;		// endpoint to concatenate to end of API URL
	private String input;			// User input
	private String apiToken;		// ID for API
	private String jsonData;		// Json data from the GET request
	
	// Constructors
	// Default Constructor to grab 10 games
	public InternetGameDataBase()
	{
		setAPIurl("https://api-v3.igdb.com");
		setInput("fields *; where id = 1942;");
		setEndPoint("games");
		setAPIToken("c9cb9e0f95bf56ac81f23e58048e1eca");
	}
	
	// Nondefault Constructor
	public InternetGameDataBase(String endpoint,String input)
	{
		setAPIurl("https://api-v3.igdb.com");
		setInput(input);
		setEndPoint(endpoint);
		setAPIToken("c9cb9e0f95bf56ac81f23e58048e1eca");
	}
	
	// Accessors
	// Get API URL
	public String getAPIurl()
	{
		return apiURL;
	}
	
	// Get user's input
	public String getInput()
	{
		return input;
	}
	
	// Get endpoint to concatenate to end of url
	public String getEndPoint()
	{
		return endpoint;
	}
	
	// Get API Token
	public String getAPIToken()
	{
		return apiToken;
	}
	
	// Get unparsed JSON
	public String getJsonData()
	{
		return jsonData;
	}
	
	
	// Mutators
	// Set API URL
	public void setAPIurl(String url)
	{
		this.apiURL = null;
		this.apiURL = new String(url);
	}
	
	// Set input
	public void setInput(String input)
	{
		this.input = null;
		this.input = new String(input);
	}
	
	// Set endpoint
	public void setEndPoint(String end)
	{
		this.endpoint = null;
		this.endpoint = new String(end);
	}
	
	// Set API token
	public void setAPIToken(String token)
	{
		this.apiToken = null;
		this.apiToken = new String(token);
	}
	
	
	// Set Json String
	public void setJsonData(String s)
	{
		this.jsonData = new String(s);
	}
	
	
	// Methods
	// Connect to API and request
	public void connect()
	{
		// OkHttpClient instance for sending/ receiving to an API
		OkHttpClient client = new OkHttpClient();
		
		// HTTP Body to be sent as a GET request
		MediaType mediaType = MediaType.parse("application/octet-stream");
		RequestBody body = RequestBody.create(mediaType, getInput());
		Request request = new Request.Builder()
		  .url(getAPIurl() + "/" + getEndPoint() + "/")
		  .get()
		  .addHeader("user-key", getAPIToken())
		  .addHeader("cache-control", "no-cache")
		  .addHeader("Postman-Token", "efef6b32-7bad-4b20-821d-8be90d3e01a7")
		  .post(body)
		  .build();
		
		// Create Response ref variable to recieve the incoming byte stream from the GET request
		Response response;
		try 
		{
			response = client.newCall(request).execute();
			setJsonData(response.body().string());
		} catch (IOException e) {
			System.out.println("Could not connect");
			e.printStackTrace();
		}
	}
	
	// Parse Methods
	// If user prompts for single game name, then return
	// just title of game name
	public String getSingleGameName(String data)
	{
		// Create JsonArray and parse game name from
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		if (data.contains("name"))
		{
			String gameName = ((JsonObject)array.get(0)).get("name").getAsString();
			return gameName;
		}
		else 
		{
			return "";
		}
	}
	
	// Get single game summary
	public String getSingleDescr(String data)
	{
		// Create JsonArray and parse game name from
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		if(data.contains("summary"))
		{
			String descr = ((JsonObject)array.get(0)).get("summary").getAsString();
			return descr;
		}
		else
		{
			return "";
		}
	}
	
	// Get storyline summary
	public String getStory(String data)
	{
		// Create JsonArray and parse game name from
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		if(data.contains("storyline"))
		{
			String story = ((JsonObject)array.get(0)).get("storyline").getAsString();
			return story;
		}
		else
		{
			return "";
		}
	}
	
	// Get IGDB URL
	public String getDatabaseURL(String data)
	{
		// Create JsonArray and parse game name from
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		if(data.contains("url"))
		{
			String url = ((JsonObject)array.get(0)).get("url").getAsString();
			return url;
		}
		else
		{
			return "";
		}
	}
	
	
	// Return an ArrayList of an array of game IDs
	public ArrayList<String> getGameIDArray(String data)
	{
		// Parse JsonArray
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		ArrayList<String> list = new ArrayList<>();
		
		// Add JsonArray elements to an ArrayList<String>
		for(int i = 0; i < array.size(); i++)
		{
			list.add(((JsonObject)array.get(i)).get("id").getAsString());
		}
		
		// Return list of elements
		return list;
	}
	
	// Return an ArrayList of an array of game names
	public ArrayList<String> getGameNameArray(String data)
	{
		// Parse JsonArray
		JsonArray array = new JsonParser().parse(data).getAsJsonArray();
		ArrayList<String> list = new ArrayList<>();
		
		// Add JsonArray elements to an ArrayList<String>
		for(int i = 0; i < array.size(); i++)
		{
			list.add(((JsonObject)array.get(i)).get("name").getAsString());
		}
		
		// Return list of elements
		return list;
	}
	
	
} // end of InternetGameDataBase class

// PircBot
class MyBot extends PircBot
{
	// Constructors
	// Default constructor that names bot
	public MyBot()
	{
		this.setName("BBot40");
	}
	
	// Overriding onMessage method in PircBot to have 
	// custom interactions with other users
	@Override
	public void onMessage(String Chennel, String sender, String login, String hostname, String message)
	{
		String input;		// Variable to hold any modified strings we need to pass
		
		// Welcome user and prompt them to interact
		if(message.matches("([Hh]i|[Hh]ey|[Hh]ello)"))
		{
			sendMessage("#testChannel3", "Hello " + sender + ", want to talk about weather? Or video game(s)?");
		}
		
		// If Weather is in the message....
		else if(message.matches(".*[Ww]eather.*"))
		{
			// Take out any instances of [Ww]eather in the string and assign to input
			input = new String(message.replaceAll("[Ww]eather(\\s)?|[\\r\\n]*", ""));
			sendMessage("#testChannel3", "The weather, huh?");
			
			// If string contains any other words besides weather, then pass as argument for constructor
			if(input.matches(".*[0-9a-zA-Z]+.*"))
			{
				WeatherMan weather = new WeatherMan(input);
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getWeather(weather.getResult()));
			}
			// Else just message users the weather of Dallas
			else
			{
				WeatherMan weather = new WeatherMan();
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getWeather(weather.getResult()));
			}
		}
		
		// If [Hh]umidity is in the string...
		else if(message.matches(".*[Hh]umidity.*"))
		{
			// ... take out [Hh]umidity and assign to input
			input = new String(message.replaceAll("[Hh]umidity(\\s)?|[\\r\\n]*", ""));
			sendMessage("#testChannel3", "The humidity, huh?");
			
			// If anything is left, then assign to input
			if (input.matches(".*[0-9a-zA-Z]+.*"))
			{
				WeatherMan weather = new WeatherMan(input);
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getHumidity(weather.getResult()));
			}
			// Else print Dallas humidity
			else
			{
				WeatherMan weather = new WeatherMan();
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getHumidity(weather.getResult()));
			}
		}
		
		// If Temperature is in the message..
		else if(message.matches(".*[Tt]emperature.*"))
		{
			// Assign [Tt]emperature-less string to input
			input = new String(message.replaceAll("[Tt]emperature(\\s)?|[\\r\\n]*", ""));
			sendMessage("#testChannel3", "The temperature, huh?");
			
			// If there is anything left, pass input as an argument to WeatherMan constructor
			if (input.matches(".*[0-9a-zA-Z]+.*"))
			{
				WeatherMan weather = new WeatherMan(input);
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getTemperature(weather.getResult()));
			}
			// Else pass Dallas temperature
			else
			{
				WeatherMan weather = new WeatherMan();
				weather.connect();
				weather.buffer(weather.getHttpURLConnection());
				weather.convertBufferedReader(weather.getBufferedReader());
				sendMessage("#testChannel3", weather.getTemperature(weather.getResult()));
			}
		}
		
		// If none of the above are in the string... check for [Gg]ames(s)?
		else if(message.matches(".*[Gg]ame(s)?.*"))
		{
			// Remove and assign input String without [Gg]ame(s)?
			input = new String(message.replaceAll("[Gg]ame(s )?|[\\r\\n]*", ""));
			
			// If Popular is left....
			if (input.matches(".*[Pp]opular.*"))
			{
				// Declare new InternetGameDataBase class and pass arguments for popular games list
				InternetGameDataBase database = new InternetGameDataBase("games", "fields name,popularity; sort popularity desc;");
				ArrayList<String> list;
				
				// Notify user of what is about to be printed
				sendMessage("#testChannel3", "Popular Games:\n");
				
				// Connect and give list of popular games
				database.connect();
				list = database.getGameNameArray(database.getJsonData());
				for(int i = 0;i < list.size();i++)
				{
					sendMessage("#testChannel3", list.get(i) + "\n");
				}
			}
			
			// If all that is left in the string is [Rr]ated...
			else if (input.matches(".*[Rr]ated.*"))
			{
				// Declare a database with rating as the passed argument. Declare a temp InternetGameDataBase class to be 
				// used for processing.
				InternetGameDataBase database = new InternetGameDataBase("games", "where rating > 75;");
				InternetGameDataBase temp;
				ArrayList<String> id;
				
				// Notify user of what is about to be printed
				sendMessage("#testChannel3", "List of Some Games:\n");
				
				// Connect database, obtain array of IDs of the best games
				database.connect();
				id = database.getGameIDArray(database.getJsonData());
				
				// For loop will take the IDs, and then instantiate new InternetGameDataBase objects
				// It will then take the information of the individual games and print them
				for (int i = 0; i < id.size();i++) 
				{				
					sendMessage("#testChannel3", "ID: " + id.get(i));
					temp = null;
					temp = new InternetGameDataBase("games","fields *; where id = " + id.get(i) + ";");
					temp.connect();
					sendMessage("#testChannel3", "Game: " + temp.getSingleGameName(temp.getJsonData()));
					sendMessage("#testChannel3", "Description: " + temp.getSingleDescr(temp.getJsonData()).substring(0, 100) + "...");
				}
			}
			
			// Print a list of games in no particular order
			else if (input.matches(".*[Ll]ist.*"))
			{
				// Instantiate InternetGameDataBase class
				InternetGameDataBase database = new InternetGameDataBase("games", "fields name; limit 10;");
				InternetGameDataBase temp;
				ArrayList<String> id;
				
				// Notify user what is to be printed
				sendMessage("#testChannel3", "List of Some Games:\n");
				
				// Connect and pass ArrayList<String> to id
				database.connect();
				id = database.getGameIDArray(database.getJsonData());
				
				// Connect temp InternetGameDataBase and print individual game names and summaries
				for (int i = 0; i < id.size();i++) 
				{				
					sendMessage("#testChannel3", "ID: " + id.get(i));
					temp = null;
					temp = new InternetGameDataBase("games","fields *; where id = " + id.get(i) + ";");
					temp.connect();
					sendMessage("#testChannel3", "Game: " + temp.getSingleGameName(temp.getJsonData()));
					sendMessage("#testChannel3", "Description: " + temp.getSingleDescr(temp.getJsonData()) + "...");
				}
			}
			
			// If user specifies an ID, print out details for the game.
			else if (input.matches(".*[0-9]+.*"))
			{
				InternetGameDataBase database = new InternetGameDataBase("games", "fields *; where id = " + input + ";");
				database.connect();
				
				sendMessage("#testChannel3", "Game: " + database.getSingleGameName(database.getJsonData()));
				sendMessage("#testChannel3", "Description: " + database.getSingleDescr(database.getJsonData()));
				sendMessage("#testChannel3", "Storyline: " + database.getStory(database.getJsonData()));
				sendMessage("#testChannel3", "URL: " + database.getDatabaseURL(database.getJsonData()));
			}
			
			// User may search for keywords in the game database
			else if (input.matches(".*[Ss]earch.*"))
			{
				input = input.replaceAll("[Ss]earch(\\s)?", "");				
				InternetGameDataBase database = new InternetGameDataBase("games", "fields *; search \"" + input + "\"; limit 10;");
				InternetGameDataBase temp;
				ArrayList<String> id;
				
				// Notify user what is to be printed
				sendMessage("#testChannel3", "Search Results...\n");
				
				// Connect and pass ArrayList<String> to id
				database.connect();
				id = database.getGameIDArray(database.getJsonData());
				
				// Connect temp InternetGameDataBase and print individual game names and summaries
				for (int i = 0; i < id.size();i++) 
				{				
					sendMessage("#testChannel3", "ID: " + id.get(i));
					temp = null;
					temp = new InternetGameDataBase("games","fields *; where id = " + id.get(i) + ";");
					temp.connect();
					sendMessage("#testChannel3", "Game: " + temp.getSingleGameName(temp.getJsonData()));
					sendMessage("#testChannel3", "Description: " + temp.getSingleDescr(temp.getJsonData()) + "...");
				}
			}
			
			// If only told Game(s), bot will talk about his favorite game
			else
			{
				InternetGameDataBase database = new InternetGameDataBase();
				database.connect();
				sendMessage("#testChannel3", "Hmmm... my favorite game?");
				sendMessage("#testChannel3", "For me, it's gotta be... " + database.getSingleGameName(database.getJsonData()));
				sendMessage("#testChannel3", "Let me tell you about it:");
				sendMessage("#testChannel3", database.getSingleDescr(database.getJsonData()));
			}
		}
		
	}
}