FROM php:8.2-apache

EXPOSE 80

# Install git
RUN apt-get update && apt-get install -y git

# Clone the minipavi repository
RUN git clone https://github.com/ludosevilla/minipavi.git .
RUN cp -r emulminitel/* /var/www/html/

# Use our custom php.ini
COPY php.ini "$PHP_INI_DIR/php.ini"
