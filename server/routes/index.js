module.exports = function(app, User)
{
    var crypto = require('crypto')
    var api_key = "4cb873a9f2f3b850997957d56d9fd308";
    var checkout_key = "4cb873a9f2f3b850997957d56d9fd308";
    var per_payment = 2000;
    var Car = require('./../models/car');
    var last_total_payment = 0;

    app.post('/api/user/login', function(req, res) {
        key = req.body.apiKey;

        console.log(key);

        if (key != api_key)
            return res.status(401).send({
                error: true,
                errormsg: 'Access denied'
            });

        User.findOne({email: req.body.email}, {_id: 0, password: 1}, function(err, user) {
            if (err)
                return res.status(505).send({
                    error: true,
                    errormsg: 'database failure'
                });

            if (!user)
                return res.status(404).send({
                    error: true,
                    errormsg: '아이디나 비밀번호가 올바르지 않습니다.'
                });

            console.log('user=' + user.password + ' body=' + req.body.password);
            if (user.password != req.body.password)
                return res.status(404).send({
                    error: true,
                    errormsg: '아이디나 비밀번호가 올바르지 않습니다.'
                });

            // have to send the token to use the api
            res.json({error: false});
        });

/*        res.json({
            "sessionKey": 'testsendKey',
            "email": req.body.email,
            "password": req.body.password
        }); */
    });

    // GET ALL BOOKS
    app.get('/api/users', function(req, res){
        User.find(function (err, users) {
            if (err)
                return res.status(500).send({
                    error: 'database failure'
                });

            res.json(users);
        })
    });

    // GET SINGLE BOOK
/*    app.get('/api/users/:user_id', function(req, res) {
        User.findOne({_id: req.params.user_id}, function (err, user) {
            if (err)
                return res.status(500).send({
                    error: 'database failure'
                });

            if (!user)
                return res.status(404).json({
                    error: 'user not found'
                });

            res.json(user);
        });
    }); */

    // GET BOOK BY AUTHOR
    app.post('/api/users/email', function(req, res) {
        if (api_key != req.body.apiKey)
            return res.status(401).send({
                error: true,
                errormsg: 'Access denied'
            });

        User.findOne({email: req.body.email}, {_id: 0, email: 1, password: 1, name: 1, phonenum: 1, carnum: 1, usinginfo: 1}, function(err, user) {
            if (err)
                return res.status(500).json({error: "database failure"});

            if (!user)
                return res.status(404).json({error: 'user not found'});

            res.json({
                error: false,
                user});
        })
    });

    app.post('/api/users/update', function(req, res) {
        if (api_key != req.body.apiKey)
            return res.status(401).send({
                error: true,
                errormsg: 'Access denied'
            });

        User.update({email: req.body.email}, { $set: req.body }, function(err, output) {
             if (err)
                return res.status(500).json({
                    error: true,
                    errormsg: "aaa database failure"
                });

            console.log(output);
            if (!output.n) return res.status(404).json({ 
                error: true,
                errormsg: 'user not found' 
            });

            res.json({
                error: false
            });
        });
    });

    // CREATE BOOK
    app.post('/api/users', function(req, res) {
/*        if (res.body.email.length == 0 ||
            res.body.password.length == 0 ||
            res.body.name.length == 0 ||
            res.body.name.phonenum == 0 ||
            res.body.name.carnum == 0)
            return res.status(500).send({
                error: 'form data are invaild'
            })

*/
        var user = new User();

        user.email = req.body.email;
        user.password = req.body.password;
        user.name = req.body.name;
        user.phonenum = req.body.phonenum;
        user.carnum = req.body.carnum;
        user.usinginfo.entrancetime = req.body.usinginfo.entrancetime;
        user.usinginfo.exittime = req.body.usinginfo.exittime;

        user.save(function(err) {
            if (err) {
                console.error(err);
                res.json({
                    error: true,
                    errormsg: 'database failure'
                });
            }

            res.json({result: 1});
        });
    });

    // UPDATE THE BOOK
    app.put('/api/books/:book_id', function(req, res){
        res.end();
    });

    // DELETE BOOK
    app.delete('/api/users', function(req, res){
        User.remove({ email: req.body.email }, function(err, output) {
            if (err)
                return res.status(500).json( {
                    error: 'database failure'
                });

            res.status(204).end();
        })
    });

    app.get('/api/cars', function(req, res) {
        if (api_key != req.body.apiKey)
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });

        Car.find(function (err, cars) {
            if (err)
                return res.status(500).json({
                    error: true,
                    errormsg: 'database failure'
                });

            res.json(cars);
        });
    })

    app.post('/api/car', function(req, res) {
        if (api_key != req.body.apiKey)
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            }); 

        Car.findOne({carnum: req.body.carnum}, {_id: 1}, function (err, car) {
            if (err) {
                return res.status(500).json({
                    error: true,
                    errormsg: 'database failure'
                });
            }

            if (!car) {
                return res.json({
                    error: true,
                    errormsg: '주차되어 있지 않은 차량입니다.'
                });
            }

            res.json({
                error: false,
                car
            });
        });
    });


    // CREATE NEW CAR
    app.post('/api/cars', function (req, res) {
        if (api_key != req.body.apiKey)
            return res.status(401).json( {
                error: true,
                errormsg: 'Access denied'
            });

        Car.findOne({carnum: req.body.carnum}, {_id: 0, entrancetime: 1}, function(err, cars) {
            if (err)
                return res.status(500).json( {
                    error: true,
                    errormsg: 'database failure'
                });

            if (cars)
                return res.json({
                    error: true,
                    errormsg: '이미 입차된 차량 입니다.'
                });


            var car = new Car();

            car.carnum = req.body.carnum;
            car.entrancetime = new Date();

            offset = car.entrancetime.getTimezoneOffset();

            console.log('time ' + car.entrancetime);
            console.log('time d =' + car.entrancetime.getTimezoneOffset());

            car.save(function(err) {
                if (err) {
                    res.json({
                        error: true,
                        errormsg: 'database failure'
                    });
                }

                res.json({
                    error: false,
                    entrancetime: car.entrancetime.getTime()
                });
            });
        }); 
    });

    app.delete('/api/cars', function(req, res) {
        Car.remove({carnum: req.body.carnum}, function(err, output) {
            if (err)
                return res.status(500).json({
                    error: true
                });

            res.status(204).end();
        });
    });

    app.post('/api/cars/exit', function(req, res) {
        if (api_key != req.body.apiKey) {
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });
        }

        console.log('key = ' + checkout_key + ' != ' + req.body.checkoutKey);
        if (checkout_key != req.body.checkoutKey) {
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });
        }

        Car.findOne({carnum: req.body.carnum}, {_id: 0, entrancetime: 1}, function(err, car) {
            if (err)
                return res.status(500).json({
                    error: true,
                    errormsg: 'Access denied'
                });

            if (!car) {
                return res.json({
                    error: true,
                    errormsg: '주차되어 있지 않은 차량 입니다.'
                });
            }

            Car.remove(car, function(err, output) {
                if (err)
                return res.status(500).json({
                    error: true
                });

                res.json({
                    error: false
                });
            });
        });
    });

    app.post('/api/cars/usingtime', function(req, res) {
        if (api_key != req.body.apiKey) {
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });
        }

        Car.findOne({carnum: req.body.carnum}, {_id: 0, entrancetime: 1}, function(err, cars) {
            if (err)
                return res.status(500).json( {
                    error: 'database failure'
                });

            if (!cars)
                return res.json({
                    error: true,
                    errormsg: '주차되어 있지 않은 차량 입니다.'
                });

            var totaltime = new Date();
            totaltime -= cars.entrancetime;
            totaltime /= (1000 * 60 * 60 * 24 * 1);

            res.json({
                error: false,
                totaltime: Math.ceil(totaltime)
            })
        }); 
    });

    app.post('/api/cars/usingpayment', function(req, res) {
        if (api_key != req.body.apiKey) {
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });
        }

        Car.findOne({carnum: req.body.carnum}, {_id: 0, entrancetime: 1}, function(err, cars) {
            if (err)
                return res.status(500).json( {
                    error: true,
                    errormsg: 'database failure'
                });

            if (!cars)
                return res.json({
                    error: true,
                    errormsg: '주차되어 있지 않은 차량 입니다.'
                });

            var totaltime = new Date();
            totaltime -= cars.entrancetime;
            totaltime /= (1000 * 60 * 60 * 24 * 1);

            last_total_payment = Math.ceil(totaltime) * per_payment;

            res.json({
                error: false,
                totalpayment: last_total_payment
            })
        }); 
    });

    app.post('/api/payment', function (req, res) {
        if (api_key != req.body.apiKey) {
            return res.status(401).json({
                error: true,
                errormsg: 'Access denied'
            });
        }

        // processing payment and return the result
        checkout_key = crypto.createHash('sha256').update(checkout_key).digest('base64');

        res.json({
            error: false,
            checkoutKey: checkout_key
        });
    });

}