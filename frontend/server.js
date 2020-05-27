const express = require('express');

const port = process.env.PORT || 8080;
const app = express();

app.use(express.static(`${__dirname}/target/dist/`));
app.get(/.*/,(req, res) => {
  res.sendfile(`${__dirname}/target/dist/index.html`)
});

app.listen(port);
