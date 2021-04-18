const newman = require('newman');
const fs = require('fs');
var maxNumberSwarms = 10;
var maxNumberParticles = 10000;
var particleIncrement = 100;
var baseUrlStringsDistributed = ["138.68.142.96"];
var baseUrlStringsNonDistributed = "\"138.68.142.96\"";
var functionNames = ["BOOTHS_FUNCTION", "BEALE", "EASOM", "THREE_HUMP_CAMEL"];
var nonDistribEndpoint = "runSwarm";
var distribEndpoint = "distributedImplementation";

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
        for (particleNumber = 100; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
            for(functionId = 0; functionId<= functionNames.length-1; functionId ++) {
                runNewmanCode(functionNames[functionId], "local", swarmNumber, particleNumber, baseString, distribEndpoint);
                var moveOntoNextTest = false;
                while (!moveOntoNextTest) {
                    moveOntoNextTest = checkIfFilesExist(functionNames[functionId], swarmNumber, particleNumber);
                    await new Promise(resolve => setTimeout(resolve, 1000));
                }
                if (particleNumber == 1000) {
                    particleIncrement = 1000;
                }
            }
        }
    }
}

async function nonDistributedRun() {
    var swarmNumber;
    var particleNumber;
    var functionId;
    for (swarmNumber = 1; swarmNumber <= maxNumberSwarms; swarmNumber++) {
        particleIncrement = 100;
        for (particleNumber = 100; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
            for(functionId = 0; functionId<= functionNames.length-1; functionId ++) {
                runNewmanCode(functionNames[functionId], "Remote", swarmNumber, particleNumber, baseUrlStringsNonDistributed);
                var moveOntoNextTest = false;
                while (!moveOntoNextTest) {
                    moveOntoNextTest = checkIfFilesExist(functionNames[functionId], swarmNumber, particleNumber, nonDistribEndpoint);
                    await new Promise(resolve => setTimeout(resolve, 1000));
                }
                if(particleNumber == 1000) {
                    particleIncrement = 1000;
                }
            }
        }
    }
}

function checkIfFilesExist(testCollectionName, numberOfSwarms, numberOfParticles){
    var dir = "./Results/" + testCollectionName + "/" +  testCollectionName + "_" + numberOfSwarms + "_" + numberOfParticles + '.result.json';

    if (fs.existsSync(dir)) {
        return true;
    } else {
        return false;
    }
}

function runNewmanCode(testCollectionName, environmentName, numberOfSwarms, numberOfParticles, baseUrlsSting, endpoint) {
    newman.run({
        collection: require('./Requests/' + testCollectionName + '.postman_collection.json'),
        environment: require('./EnvVariables/' + environmentName + '.postman_environment.json'),
        reporters: 'cli',
        globalVar: [{ "key":"NumberOfSwarms", "value":numberOfSwarms }, {"key":"endpoint", "value": endpoint}, { "key":"NumberOfParticles", "value": numberOfParticles}, { "key":"BaseUrls", "value": baseUrlsSting}]
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
            var dir = "./Results/" + testCollectionName + "/" ;
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