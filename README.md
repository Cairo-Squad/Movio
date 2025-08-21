<p align="center">
    <img width="480" height="270" alt="cover" src="https://github.com/user-attachments/assets/bc8f4631-309f-4011-b0ac-bccb3dbff243" />
    <p align="center">
    Movio is an Android app designed for movie and TV enthusiasts. It lets you discover and explore a vast collection of trending films and shows powered by The Movie Database (TMDb) API. With its modern and intuitive interface, Movio makes finding your next watch simple and enjoyable. Whether you’re in the mood for a blockbuster or a hidden gem, Movio is your go-to companion for everything movies and TV.
  </p>
  <br>
</p>

## Architecture
This project is built using Multi-module architecture (modularization by component)
<img width="1483" height="580" alt="by_component (1)" src="https://github.com/user-attachments/assets/e02974f9-2570-4815-be1e-a789c660af5c" />


## Technologies
> [!TIP]
> - Jetpack Compose
> - Room Database
> - Paging3
> - Dagger/hilt
> - Firebase (Analytic & Crashlytics)
> - Firebase ML kit
> - Retrofit & Okhttp
> - Coil
> - Lottie Compose
> - Compose Navigation 2
> - Deep Link

## Features
### OnBoarding
| On Boarding 1                                                                                                                            | On Boarding 2                                                                                                                              |
|------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="Pixel 9 Pro" src="https://github.com/user-attachments/assets/653718dd-62e1-4c0e-8186-4ed461f08bef" /> | <img width="384" height="776" alt="on boarding 2" src="https://github.com/user-attachments/assets/b7562f07-fab5-4015-8fb6-89ef2a91d5cd" /> |


### Authentication
| Login                                                                                                                              | Sign up                                                                                                                             | Forget Password                                                                                                                              |
|------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="login" src="https://github.com/user-attachments/assets/ba74151f-60b0-4294-8be8-809cc1bde3ca" /> | <img width="384" height="776" alt="signup" src="https://github.com/user-attachments/assets/d1b1d4cd-3fcb-4274-bf69-2a78167e1bb8" /> | <img width="384" height="776" alt="forget password" src="https://github.com/user-attachments/assets/aa73f59e-8c63-4bb6-9d37-c0c70e276fc1" /> |
> [!Note]
>
> - **Login**: So you can login to your TMDB account.
> - **Sign up**: So you can create new account if you don't have one yet.
> - **Forget Password**: So you can easily restore your password if you forgot it.


### Home Screen
| Home Movies                                                                                                                              | Home Tv Shows                                                                                                                              | Home Categories                                                                                                                              |
|------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="home movies" src="https://github.com/user-attachments/assets/155c55a2-2f44-4a46-9be2-833ecd484a38" /> | <img width="384" height="776" alt="home tv shows" src="https://github.com/user-attachments/assets/96c36a18-90b1-47a9-a669-d5343ac283b4" /> | <img width="384" height="776" alt="home categories" src="https://github.com/user-attachments/assets/3aeb7925-3c30-406d-8c91-9616d804f877" /> |
> [!Note]
>
> - **Top Rating**: Displays the top rated movies/tv shows for you.
> - **Now Playing**: Displays all new movies/tv shows.
> - **Upcoming**: Displays the new movies/tv shows that are going to be released soon.
> - **More Recommended**: Displays recommended movies/tv shows based on your taste.


### Search
| Explore                                                                                                                              | Search                                                                                                                              | Search Results                                                                                                                              | For You                                                                                                                              |
|--------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="explore" src="https://github.com/user-attachments/assets/2509847a-72fc-4af4-bf33-5d5c817b3704" /> | <img width="384" height="776" alt="search" src="https://github.com/user-attachments/assets/14f28034-222b-403c-8b7c-98461b5b420f" /> | <img width="384" height="776" alt="search results" src="https://github.com/user-attachments/assets/d00dd1d0-f309-4a3f-958c-4c42508d5bf9" /> | <img width="384" height="776" alt="for you" src="https://github.com/user-attachments/assets/cefc37c0-d3ba-47e0-95c4-9040943948a5" /> |
> [!Note]
>
> - Displays personalized movies and tv shows based on your previous watch history.
> - Recommend some new trending and top rated movies and tv shows.
> - Auto complete for your search results.
> - Displays the search results for movies, tv shows and actors.

### Details Screens
#### Movie Details:
> **_Displays all the details about the movie and you can share it with your friends._**
<table>
  <thead>
    <tr>
      <th>Movies Details 1</th>
      <th>Movie Details 2</th>
      <th>Similar Movies</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <img width="384" height="776" alt="movie details 1" src="https://github.com/user-attachments/assets/0345079a-51f2-4e73-929a-42f63ca5f86a" />
      </td>
      <td>
        <img width="384" height="776" alt="movie details 2" src="https://github.com/user-attachments/assets/96653772-200e-432f-aceb-af6c6085972b" />
      </td>
      <td>
        <img width="384" height="776" alt="similar movies" src="https://github.com/user-attachments/assets/72c8c7e0-f291-4e49-b6a1-65fde26f2ea6" />
      </td>
    </tr>
  </tbody>
</table>

> [!Note]
>
> - **Basic Details**: Displays the movie details and every thing related to it.
> - **Similar Movies**: Displays similar movies, the actors whom worked in this project.


#### TV Show Details:
> **_Displays all the details about the tv show and you can share it with your friends._**
<table>
  <thead>
    <tr>
      <th>Tv show details 1</th>
      <th>Tv show details 2</th>
      <th>Seasons</th>
      <th>Episodes</th>
      <th>Similar Tv shows</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <img width="384" height="776" alt="tv show details 1" src="https://github.com/user-attachments/assets/34069c2e-4a62-4596-9ea3-5124d29db77e" />
      </td>
      <td>
        <img width="384" height="776" alt="tv show details 2" src="https://github.com/user-attachments/assets/81848a33-b641-49d8-bd95-c82bee3e1ed4" />
      </td>
      <td>
        <img width="384" height="776" alt="seasons" src="https://github.com/user-attachments/assets/07a23403-c3f3-4156-baff-b1c09b14d386" />
      </td>
      <td>
        <img width="384" height="776" alt="episodes" src="https://github.com/user-attachments/assets/88f7323d-868f-4eae-a3fc-6f32bc4c57c1" />
      </td>
      <td>
        <img width="384" height="776" alt="similar tv shows" src="https://github.com/user-attachments/assets/1c26a317-5ec0-48a7-bdab-8dd041e18e96" />
      </td>
    </tr>
  </tbody>
</table>

> [!Note]
>
> - **Basic Details**: Displays the tv show details and every thing related to it.
> - **Similar Movies**: Displays similar tv shows, the actors whom worked in this project and also it's seasons.
> - **Seasons**: You can select easily the season you want to watch.
> - **Episode**: Displays all the episodes that is inside this season.



### Library:
| Library (Guest)                                                                                                                            | Library (Logged In)                                                                                                                            | Lists                                                                                                                              | Favorites                                                                                                                              | History                                                                                                                              |
|--------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="library guest" src="https://github.com/user-attachments/assets/3d824337-3c81-4643-8350-51c59ecdd577" /> | <img width="384" height="776" alt="library logged in" src="https://github.com/user-attachments/assets/16e24d73-b185-4e8b-92d8-8ec00d2fca87" /> | <img width="384" height="776" alt="lists" src="https://github.com/user-attachments/assets/1a02fad9-bacb-4849-be9c-d4a943a4a815" /> | <img width="384" height="776" alt="favorites" src="https://github.com/user-attachments/assets/1277192e-4d8c-4a53-be03-e93f616b9563" /> | <img width="384" height="776" alt="history" src="https://github.com/user-attachments/assets/50438e46-fd06-4abf-9cf4-db8c18c777f1" /> |
> [!Note]
>
> - Displays all of your lists, favorite items and your watch history.
> - Check the lists you have and the items inside of it.
> - Display all of your favorite movies and tv shows.
> - Display your watch history for everything you have watched.




### Profile Screen:
| Profile (Guest)                                                                                                                            | Profile (Logged In)                                                                                                                            | Theme Switch                                                                                                                              | Language Change                                                                                                                              |
|--------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="profile guest" src="https://github.com/user-attachments/assets/e5b380b2-8acb-4b26-bffb-ac2c5f59f21c" /> | <img width="384" height="776" alt="profile logged in" src="https://github.com/user-attachments/assets/cf490858-d0b4-4c5b-aa79-d31dfd8b0c50" /> | <img width="384" height="776" alt="change theme" src="https://github.com/user-attachments/assets/d96e373a-7f5d-4bc0-949a-f94e8d1cfa73" /> | <img width="384" height="776" alt="change language" src="https://github.com/user-attachments/assets/352b90f3-fa09-4691-b9bb-b1407c95b167" /> |
> [!Note]
>
> - You displays your image and user name.
> - Can change the application theme to light or dark.
> - Can switch easily the language of the application to arabic or english.



### My Ratings:
| Rating Empty                                                                                                                              | Rating filled                                                                                                                              |
|-------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="384" height="776" alt="rating empty" src="https://github.com/user-attachments/assets/1b254e9e-0e93-4d87-8956-d2477f96d783" /> | <img width="384" height="776" alt="rating filled" src="https://github.com/user-attachments/assets/40206fd6-abc6-4ddc-934e-3b00abc0b5ca" /> |
> [!Note]
>
> - You can easily know what series or movies you have rated.
> - Easily to remove the item you don't want by swiping it.



### Islamic Image Viewer:
| Islamic Image Viewer                                                                                                                              |
|---------------------------------------------------------------------------------------------------------------------------------------------------| 
| <img width="384" height="776" alt="islamic image viewer" src="https://github.com/user-attachments/assets/2a9af823-1869-4990-a0cf-905b17bbc379" /> |
> [!Note]
>
> - AI-driven content detection aligned with Islamic guidelines
> - Intelligent image blurring for sensitive material
> - Coil-based image loading with customizable transformations
> - Adjustable sensitivity settings


---
## Installation
1- Clone the Repository
```
git clone https://github.com/Cairo-Squad/Movio
```
2- open the project in android studio.
3- Add the required parameters in `local.properties` file.
**Add required properties**
   In the project root, open or create a `local.properties` file and add the following:

   ```properties
   IMAGE_BASE_URL="https://image.tmdb.org/t/p/w500/"
   BASE_URL = "https://api.themoviedb.org/3/"
   API_KEY = "You Own API Key"
   ```
   > To obtain your `YOUR_API_KEY`:
   >
   > 1. Create an account at [The Movie Database (TMDB)](https://www.themoviedb.org/signup)
   > 2. Go to **Settings → API** in your TMDB account
   > 3. Generate a **API Key** and paste it as `apiKey` above.

4- Build and run the project on an emulator or physical device.

## Contributors
<a href="https://github.com/Cairo-Squad/Movio/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Cairo-Squad/Movio" />
</a>