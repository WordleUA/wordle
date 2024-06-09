FROM node:14 AS build

WORKDIR /app
COPY ./package.json ./
COPY ./ ./

RUN npm install && npm run build

# Фаза запуску
FROM node:14 AS serve

WORKDIR /app
COPY --from=build /app/build ./build

# Встановлюємо HTTP сервер для обслуговування статичних файлів
RUN npm install -g serve

EXPOSE 3000

CMD ["serve", "-s", "build", "-l", "3000"]
