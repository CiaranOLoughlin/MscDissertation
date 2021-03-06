const newman = require('newman');
const fs = require('fs');

var maxNumberSwarms = 10;
var maxNumberParticles = 100000;
var particleIncrement = 1000;
var particleStartNumber = 1000;

var baseUrlStringsDistributed = ["138.68.142.96", "165.227.230.182", "165.227.238.19", "165.227.224.249", "165.227.230.17", "138.68.189.16", "138.68.157.95", "138.68.157.219", "138.68.187.214", "165.227.228.78"];
var baseUrlStringsNonDistributed = "\"138.68.142.96\"";

var functionNames = ["BOOTHS_FUNCTION","BEALE", "EASOM"];

var nonDistribEndpoint = "runSwarm";
var distribEndpoint = "distributedImplementation";

nonDistributedRun();
distributedRun();

async function distributedRun() {
    var swarmNumber;
    var particleNumber;
    for(var functionId = 0; functionId<= functionNames.length-1; functionId ++) {
        var baseString = '';
        for (swarmNumber = 0; swarmNumber <= maxNumberSwarms; swarmNumber++) {
            particleIncrement = 1000;
            if (baseString != '') {
                baseString = baseString + ',';
            }
            baseString = baseString + '"' + baseUrlStringsDistributed[swarmNumber] + '"';

            for (particleNumber = particleStartNumber; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
                runNewmanCode("DISTRIB", functionNames[functionId], "remote_distrib", swarmNumber + 1, particleNumber, baseString, distribEndpoint);
                var moveOntoNextTest = false;
                while (!moveOntoNextTest) {
                    moveOntoNextTest = checkIfFilesExist("DISTRIB",functionNames[functionId], swarmNumber + 1, particleNumber);
                    await new Promise(resolve => setTimeout(resolve, 1000));
                }
            }
        }
    }
}

async function nonDistributedRun() {
    for(var functionId = 0; functionId<= functionNames.length-1; functionId ++) {
        for (var swarmNumber = 1; swarmNumber <= maxNumberSwarms; swarmNumber++) {
            particleIncrement = 1000;
            for (var particleNumber = particleStartNumber; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
                runNewmanCode("NON_DISTRIB", functionNames[functionId], "Remote", swarmNumber, particleNumber, baseUrlStringsNonDistributed, nonDistribEndpoint);
                var moveOntoNextTest = false;
                while (!moveOntoNextTest) {
                    moveOntoNextTest = checkIfFilesExist("NON_DISTRIB", functionNames[functionId], swarmNumber, particleNumber);
                    await new Promise(resolve => setTimeout(resolve, 1000));
                }
            }
        }
    }
}

function checkIfFilesExist(envName, testCollectionName, numberOfSwarms, numberOfParticles){
    var dir = "./Results/" + envName + "/" + testCollectionName + "/" +  testCollectionName + "_" + numberOfSwarms + "_" + numberOfParticles + '.result.json';

    if (fs.existsSync(dir)) {
        return true;
    } else {
        return false;
    }
}

function runNewmanCode(envName, testCollectionName, environmentName, numberOfSwarms, numberOfParticles, baseUrlsSting, endpoint) {
    newman.run({
        collection: require('./Requests/' + testCollectionName + '.postman_collection.json'),
        environment: require('./EnvVariables/' + environmentName + '.postman_environment.json'),
        reporters: 'cli',
        globalVar: [{ "key":"NumberOfSwarms", "value":numberOfSwarms }, {"key":"endpoint", "value": endpoint}, { "key":"NumberOfParticles", "value": numberOfParticles}, { "key":"BaseUrls", "value": baseUrlsSting}]
    }).on('beforeRequest', function (error, args) {
        if (error) {
            console.error(error);
        }
    }).on('request', function (error, args) {
        if (error) {
            console.error(error);
        }
        else {
            var dir = "./Results/" + envName + "/" + testCollectionName + "/" ;
            if (!fs.existsSync(dir)){
                fs.mkdirSync(dir);
            }

            fs.writeFile( dir + testCollectionName + "_" + numberOfSwarms + "_" + numberOfParticles + '.result.json', args.response.stream, function (error) {
                if (error) {
                    console.error(error);
                }
            });
        }
    });
}