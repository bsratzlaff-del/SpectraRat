# STAGE 1: Build the Angular app
FROM node:20-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy source code and build for production
COPY . .
RUN npm run build --configuration=production

# STAGE 2: Serve with Nginx
FROM nginx:alpine

# Remove the default Nginx index page
RUN rm -rf /usr/share/nginx/html/*

# Copy the compiled Angular files from the build stage
# NOTE: Check your 'dist' folder. With the new Angular builder, it might be inside a 'browser' subfolder.
COPY --from=build /app/dist/spectrarat-ui/browser /usr/share/nginx/html

# Copy our custom Nginx configuration (we will create this next!)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]