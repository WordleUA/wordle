FROM node:14 AS build-frontend

WORKDIR /app
COPY ./package.json ./
COPY ./ ./

RUN npm install && npm run build