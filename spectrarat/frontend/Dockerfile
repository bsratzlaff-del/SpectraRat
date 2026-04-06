# STAGE 1: Build the Angular App
FROM node:20-alpine AS build
WORKDIR /app
# Copy dependency lists and install them
COPY package*.json ./
RUN npm install
# Copy all your source code (including your fixed HTML!) and compile it
COPY . .
RUN npm run build --configuration=production

# STAGE 2: Serve with Nginx
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# Grab the freshly compiled 'dist' folder from Stage 1
COPY --from=build /app/dist/spectrarat-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80