FROM php:8.2-cli

VOLUME /config
VOLUME /logs
VOLUME /stats
VOLUME /recordings

# Install git
RUN apt-get update && apt-get install -y git

# Install the PHP extensions we need
RUN docker-php-ext-configure pcntl --enable-pcntl \
  && docker-php-ext-install \
    pcntl

# Clone the minipavi repository
WORKDIR /app
RUN git clone https://github.com/ludosevilla/minipavi.git .

CMD [ "php", "./minipavi.php", "--cfgfile", "/config/minipavi.xml"]
