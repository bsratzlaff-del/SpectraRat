# STAGE 1: Use a simple Node image
FROM node:20-alpine AS build
WORKDIR /app
COPY . .
# We are skipping 'npm run build' because we're going to use your local build if needed
# OR just ensure your angular.json is fixed.

# STAGE 2: Serve with Nginx
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# If you have a 'dist' folder already zipped up, we copy that:
COPY --from=build /app/dist/spectrarat-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
