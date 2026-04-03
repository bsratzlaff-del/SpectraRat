# Stage 1: Build the Angular application
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Stage 2: Serve the application from a lightweight Nginx server
FROM nginx:1.25-alpine
COPY --from=build /app/dist/spectrarat-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80