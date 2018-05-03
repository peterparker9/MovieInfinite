# MovieInfinite

An app to display Movie details of all available on TMDB.
Infinite scroll and also, the data is infinite
Fast scroll is enabled. 
Used Realm for local storage
In case of no network, of data present in realm, it would be populated from local db instead of fetching from API

Tech Stack used:
-- Android Studio, Java
--- Libraries
  - Glide for image
  - Realm for db orm
  - Retrofit 2 for network call
  - fasterxml for parsing and using jsonignoreproperties
  
Technical Solution

- I have used MVP pattern along with repository design pattern.
- In case data is present in db, no network call is required
- If network call is done, it persist data in local db first
- Thought of using Room, Livedata- mvvm architecture, but was able to solve by this method itself, so adhered to it.
  
