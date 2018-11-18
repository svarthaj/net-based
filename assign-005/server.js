const http = require('http');
const fs = require('fs');
const { parse } = require('querystring');

fs.readFile('./index.html', function (err, html) {
  if (err) throw err;
  http.createServer((req, res) => {
      if (req.method === 'POST') {
          // Handle post info...
          let body = '';
          req.on('data', chunk => {
            body += chunk.toString(); // convert Buffer to string
          });
          req.on('end', () => {
            // async file read
            fs.readFile('./phonebook.txt', function (err, phones) {
              // Parse the data from txt file into object array and compare with queried data
              if (err) throw err;
              var query = parse(body), // parse query body into object
                  catalog = parseCSV(phones.toString());
              for (var i=0; i<catalog.length; i++) {
                if (query.lastname === catalog[i].lname &&
                    query.firstname === catalog[i].fname) {
                      res.write(
                        ` <!DOCTYPE html>
                          <html>
                          <body>
                          <h2> ${catalog[i].fname} ${catalog[i].lname} phone is ${catalog[i].phone}</h2>
                          <button type="button" onclick="history.back()">Go Back</button>
                          </body>
                          </html>`);
                      res.end();
                }
              }
              // if user was not found, send error
              res.end(` <!DOCTYPE html>
                <html>
                <body>
                <h2>User Not Found</h2>
                <button type="button" onclick="history.back()">Go Back</button>
                </body>
                </html>`);
            });
          });
      }
      else {
        res.writeHeader(200, {"Content-Type": "text/html"});
        res.write(html);
        res.end();
      }
  }).listen(8080, function() {
    console.log('Server running at http://localhost:8080');
  });
});

var parseCSV = function(input) {
  var catalog = [],
      lines = input.split(/\r\n|\r|\n/);
  for (var i=0; i<lines.length; i++) {
    var data = lines[i].split(',');
    var record = {
      lname: data[0],
      fname: data[1],
      phone: data[2]
    };
    catalog.push(record);
  }
  return catalog;
}