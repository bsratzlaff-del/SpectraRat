FROM node:20-alpine AS build
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy all source code into the container
COPY . .

# Build the Angular app right here inside the container
RUN npm run build


FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*

# Copy the fresh build from Stage 1
COPY --from=build /app/dist/spectrarat-ui/browser /usr/share/nginx/html

# Copy your specific Nginx routing configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]