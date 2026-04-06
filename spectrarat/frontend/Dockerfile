FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
# This copies the folder you JUST built on your laptop
COPY dist/spectrarat-ui/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80