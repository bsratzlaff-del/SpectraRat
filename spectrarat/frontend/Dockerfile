# Use Nginx to serve the static files
FROM nginx:alpine

# 1. Clean the default Nginx location
RUN rm -rf /usr/share/nginx/html/*

# 2. Copy the ALREADY BUILT files from your laptop to the container
# Note: Ensure this path matches your 'Output location' from the green text
COPY dist/spectrarat-ui/browser /usr/share/nginx/html

# 3. Copy your fixed Nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]