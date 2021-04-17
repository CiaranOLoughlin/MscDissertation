const newman = require('newman');
const fs = require('fs');
var maxNumberSwarms = 2;
var maxNumberParticles = 200;
var particleIncrement = 100;
var baseUrlStringsDistributed = ["138.68.142.96"];
var baseUrlStringsNonDistributed = "\"138.68.142.96\"";
var functionNames = ["BOOTHS_FUNCTION", "BEALE", "EASOM", "THREE_HUMP_CAMEL"];

nonDistributedRun();
//distributedRun();

async function distributedRun() {
    var swarmNumber;
    var particleNumber;
    var baseString = '';
    for (swarmNumber = 0; swarmNumber <= baseUrlStrings.length - 1; swarmNumber++) {
        if (baseString != '') {
            baseString = baseString + ','
        }
        baseString = baseString + '"' + baseUrlStringsDistributed[swarmNumber] + '"';
        console.log(baseString);
        for (particleNumber = 100; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
            runNewmanCode("DistributedCollection", "local", swarmNumber, particleNumber, baseString);
            var moveOntoNextTest = false;
            while (!moveOntoNextTest) {
                moveOntoNextTest = checkIfFilesExist("DistributedCollection", swarmNumber, particleNumber);
                await new Promise(resolve => setTimeout(resolve, 1000));
            }
        }
    }
}

async function nonDistributedRun() {
    var swarmNumber;
    var particleNumber;
    for (swarmNumber = 1; swarmNumber <= maxNumberSwarms; swarmNumber++) {
        for (particleNumber = 100; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
            runNewmanCode("NonDistributedCollection", "Remote", swarmNumber, particleNumber, baseUrlStringsNonDistributed);
            var moveOntoNextTest = false;
            while (!moveOntoNextTest) {
                moveOntoNextTest = checkIfFilesExist("NonDistributedCollection", swarmNumber, particleNumber);
                await new Promise(resolve => setTimeout(resolve, 1000));
            }
        }
    }
}

function checkIfFilesExist(testCollectionName, numberOfSwarms, numberOfParticles){
    var particlePath = "Particle_" + numberOfParticles + "/";
    var dir = "./Results/" + testCollectionName + "/Swarm_" + numberOfSwarms + "/" ;
    dir = dir + particlePath;
    var numberOfExistingFiles = 0;

    for(var i =0; i<functionNames.length-1;i++) {
        var path = dir + functionNames[i] + ".result.json";
        if (fs.existsSync(path)) {
            numberOfExistingFiles ++;
        }
    }
    if(numberOfExistingFiles == functionNames.length-1){
        return true;
    }
    else {
        return false;
    }
}

function runNewmanCode(testCollectionName, environmentName, numberOfSwarms, numberOfParticles, baseUrlsSting) {
    newman.run({
        collection: require('./Requests/' + testCollectionName + '.postman_collection.json'),
        environment: require('./EnvVariables/' + environmentName + '.postman_environment.json'),
        reporters: 'cli',
        globalVar: [{ "key":"NumberOfSwarms", "value":numberOfSwarms }, { "key":"NumberOfParticles", "value": numberOfParticles}, { "key":"BaseUrls", "value": baseUrlsSting}]
    }).on('beforeRequest', function (error, args) {
        console.log(args.request.body);
        if (error) {
            console.error(error);
        }
    }).on('request', function (error, args) {
        if (error) {
            console.error(error);
        }
        else {
            var particlePath = "Particle_" + numberOfParticles + "/"
            var dir = "./Results/" + testCollectionName + "/Swarm_" + numberOfSwarms + "/" ;
            if (!fs.existsSync(dir)){
                fs.mkdirSync(dir);
            }
            dir = dir + particlePath;
            if (!fs.existsSync(dir)){
                fs.mkdirSync(dir);
            }

            fs.writeFile( dir + args.item.name + '.result.json', args.response.stream, function (error) {
                if (error) {
                    console.error(error);
                }
            });
        }
    });
}