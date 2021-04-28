const fs = require('fs');

var numberOfSwarms = 10;
var maxNumberParticles = 500000;
var outputString = "SWARM_NUMBER,PARTICLE_NUMBER,CENTRALISED_ITERATIONS,CENTRALISED_TIME_TO_SOLUTION,CENTRALISED_FINAL_GROUP_BEST,DISTRIBUTED_ITERATIONS,DISTRIBUTED_TIME_TO_SOLUTION,DISTRIBUTED_FINAL_GROUP_BEST\n";
var functionName = "BEALE";
var particleIncrement = 500;

for (var swarmNumber = 1; swarmNumber <= numberOfSwarms; swarmNumber++) {
    for (var particleNumber = 1000; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
        var fileUrlCentralised = './Results/NON_DISTRIB/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlDistributed = './Results/DISTRIB/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var doNothing = false;
        if (fs.existsSync(fileUrlCentralised) && fs.existsSync(fileUrlDistributed)) {

        } else {
            doNothing=true;
        }
        if(!doNothing) {
            let centralised = fs.readFileSync(fileUrlCentralised);
            let centralised_data = JSON.parse(centralised);
            outputString = outputString + swarmNumber  + "," + particleNumber + "," + centralised_data.iterations + "," + centralised_data.timeToSolution + "," + centralised_data.finalGroupBest + ",";


            let distributed = fs.readFileSync(fileUrlDistributed);
            let distributed_data = JSON.parse(distributed);
            outputString = outputString + distributed_data.iterations + "," + distributed_data.timeToSolution + "," + distributed_data.finalGroupBest + "\n";
        }
    }
}

fs.writeFile('./Results/' + functionName + '_FINAL_2.0.csv', outputString, function (err) {
    if (err) return console.log(err);
    console.log('Done');
});