# Popular Movies
Popular Movies project for Google Associate Android Developer Fast Track Nanodegree Program

### This project is using the following library:
+ <a href="http://square.github.io/picasso/">Picasso</a>
+ <a href="http://square.github.io/retrofit/">Retrofit</a>
+ <a href="http://jakewharton.github.io/butterknife/">Butterknife</a>

### Configuration:
+ Create your API key file
``` 
apiKey = <your api key> 
```
+ Change this line on ``` build.gradle ``` file with your <a href="https://www.themoviedb.org">themoviedb.com</a> API key path
```java
Properties properties = new Properties()
    properties.load(new FileInputStream("/Users/harismawan/Developer/ApiKey/themoviedb"))
```

### Screenshot:
+ Main menu

![alt text](https://github.com/AFHarismawan/PopularMovies/blob/master/screenshot/Screenshot_1503812219.png)
+ Movie detail

![alt text](https://github.com/AFHarismawan/PopularMovies/blob/master/screenshot/Screenshot_1503812230.png)
+ Add to favorite

![alt text](https://github.com/AFHarismawan/PopularMovies/blob/master/screenshot/Screenshot_1503812233.png)
+ Remove from favorite

![alt text](https://github.com/AFHarismawan/PopularMovies/blob/master/screenshot/Screenshot_1503812244.png)
+ Videos popup

![alt text](https://github.com/AFHarismawan/PopularMovies/blob/master/screenshot/Screenshot_1503812251.png)
