const pino = require("pino");
const config = require("./config");

const logger = pino({
  level: config.logLevel,
  base: {
    service: config.appName,
    env: config.env
  }
});

module.exports = logger;