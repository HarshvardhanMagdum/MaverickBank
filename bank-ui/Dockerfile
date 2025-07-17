# Stage 1: Build the React application
# Use a Node.js image to build the React app
FROM node:18-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock) to install dependencies
# This step is cached if dependencies haven't changed, speeding up builds
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the React application for production
# Assuming your React app uses 'npm run build' to create a 'dist' folder (common with Vite)
# If your build output is 'build', change 'dist' to 'build' below.
RUN npm run build

# Stage 2: Serve the React application with Nginx
# Use a lightweight Nginx image to serve the static files
FROM nginx:alpine

# Copy the built React app from the 'build' stage to Nginx's public directory
# The 'dist' directory contains the optimized static assets (change to 'build' if needed)
COPY --from=build /app/dist /usr/share/nginx/html

# Copy a custom Nginx configuration file (optional, but good for single-page apps)
# This configuration ensures that all routes are handled by index.html,
# which is typical for React Router or similar client-side routing.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80, which Nginx listens on by default
EXPOSE 80

# The default command for Nginx is to start it in the foreground
CMD ["nginx", "-g", "daemon off;"]
