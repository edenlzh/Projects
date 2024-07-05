# cs732-assignment-edenlzh
# Vue Gaming Article Viewer

This project demonstrates basic features such as listing articles, adding new articles, and navigating between different pages. The application makes use of Vue Router for navigation and Vuex for state management. The project also showcases the usage of components and Vue's reactivity system.

## Features

- List articles with a brief summary and an image
- View individual articles with detailed content and a larger image
- Add new articles with a title and content
- Navigate to a gallery page showing images from all articles
- Responsive design for both desktop and mobile devices

## Getting Started

Follow these steps to get the project up and running on your local machine:

### Prerequisites

Make sure you have Node.js and npm installed on your system. You can download Node.js from the [official website](https://nodejs.org/) and npm will be included with it.

### Installation

1. Clone the repository:

```
git clone https://github.com/UOA-CS732-SE750-Students-2024/cs732-assignment-edenlzh.git
```

2. Change to the project directory:
```
cd cs732-assignment-edenlzh-main
```

3. Install the required dependencies:
```
npm install
```

4. Start the development server:
```
npm run serve
```

5. Open your browser and navigate to [http://localhost:8080](http://localhost:8080) to view the running application.


## Implementation Details

- The application is built using Vue.js, a popular JavaScript framework for building user interfaces.
- Vue Router is used for handling navigation between pages.
- Vuex is used for managing the application's state and storing articles.
- Components are used to modularize and reuse code throughout the application.
- The project uses `localStorage` for storing article data on the client-side.
- Vue's reactivity system is leveraged to ensure that changes in the application's state are automatically reflected in the user interface.

## Project Structure

The project is structured as follows:

- `src`: Contains the main source code for the application.
  - `assets`: Contains images and other static assets.
  - `components`: Contains reusable Vue components used throughout the application.
  - `mock`: Contains mock data for articles.
  - `router`: Contains the Vue Router configuration for handling navigation between pages.
  - `store`: Contains the Vuex store configuration for managing application state.
  - `utils`: Contains utility functions for working with articles.
  - `views`: Contains the main view components that make up the application's pages.
- `public`: Contains the main `index.html` file and other public assets.

### Key Files

- `src/main.js`: The entry point for the application. Sets up the Vue instance, router, and store.
- `src/App.vue`: The root component for the application.
- `src/router/index.js`: The Vue Router configuration file.
- `src/store/modules/article.js`: The Vuex store module for managing articles.
- `src/views/Article/ArticlePage.vue`: The main view component for displaying individual articles.
- `src/views/AddArticle/index.vue`: The main view component for adding new articles.
- `src/views/Gallery/index.vue`: The main view component for displaying the gallery of article images.