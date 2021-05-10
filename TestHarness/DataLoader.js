const fs = require('fs');

var numberOfSwarms = 10;
var maxNumberParticles = 500000;
var outputString = "SWARM_NUMBER,PARTICLE_NUMBER,CENTRALISED_ITERATIONS,CENTRALISED_TIME_TO_SOLUTION,CENTRALISED_FINAL_GROUP_BEST,DISTRIBUTED_ITERATIONS,DISTRIBUTED_TIME_TO_SOLUTION,DISTRIBUTED_FINAL_GROUP_BEST\n";
var functionName = "BOOTHS_FUNCTION";
var particleIncrement = 500;

for (var swarmNumber = 1; swarmNumber <= numberOfSwarms; swarmNumber++) {
    for (var particleNumber = 1000; particleNumber <= maxNumberParticles; particleNumber += particleIncrement) {
        var fileUrlCentralised_RUN1 = './Results/NON_DISTRIB/RUN_1/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlCentralised_RUN2 = './Results/NON_DISTRIB/RUN_2/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlCentralised_RUN3 = './Results/NON_DISTRIB/RUN_3/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlDistributed_RUN1 = './Results/DISTRIB/RUN_1/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlDistributed_RUN2 = './Results/DISTRIB/RUN_2/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var fileUrlDistributed_RUN3 = './Results/DISTRIB/RUN_3/' + functionName + '/' + functionName + '_' + swarmNumber + '_' + particleNumber + '.result.json';
        var doNothing = false;
        if (fs.existsSync(fileUrlCentralised_RUN1) && fs.existsSync(fileUrlCentralised_RUN2) && fs.existsSync(fileUrlCentralised_RUN3) &&
            fs.existsSync(fileUrlDistributed_RUN1) && fs.existsSync(fileUrlDistributed_RUN2) && fs.existsSync(fileUrlDistributed_RUN3)) {

        } else {
            doNothing=true;
        }
        if(!doNothing) {
            let centralised_RUN1 = fs.readFileSync(fileUrlCentralised_RUN1);
            let centralised_RUN2 = fs.readFileSync(fileUrlCentralised_RUN2);
            let centralised_RUN3 = fs.readFileSync(fileUrlCentralised_RUN3);
            let centralised_data_RUN1 = JSON.parse(centralised_RUN1);
            let centralised_data_RUN2 = JSON.parse(centralised_RUN2);
            let centralised_data_RUN3 = JSON.parse(centralised_RUN3);
            var averageCentralisedIterations = Math.round((centralised_data_RUN1.iterations + centralised_data_RUN2.iterations + centralised_data_RUN3.iterations)/3);
            var averageCentralisedTTS = Math.round((centralised_data_RUN1.timeToSolution + centralised_data_RUN2.timeToSolution + centralised_data_RUN3.timeToSolution)/3);
            var averageCentralisedGbest = Math.round((centralised_data_RUN1.finalGroupBest + centralised_data_RUN2.finalGroupBest + centralised_data_RUN3.finalGroupBest)/3);

            outputString = outputString + swarmNumber  + "," + particleNumber + "," + averageCentralisedIterations + "," + averageCentralisedTTS + "," + averageCentralisedGbest + ",";

            let distributed_RUN1 = fs.readFileSync(fileUrlDistributed_RUN1);
            let distributed_RUN2 = fs.readFileSync(fileUrlDistributed_RUN2);
            let distributed_RUN3 = fs.readFileSync(fileUrlDistributed_RUN3);
            let distributed_data_RUN1 = JSON.parse(distributed_RUN1);
            let distributed_data_RUN2 = JSON.parse(distributed_RUN2);
            let distributed_data_RUN3 = JSON.parse(distributed_RUN3);
            var averageDistributedIterations = Math.round((distributed_data_RUN1.iterations + distributed_data_RUN2.iterations + distributed_data_RUN3.iterations)/3);
            var averageDistributedTTS = Math.round((distributed_data_RUN1.timeToSolution + distributed_data_RUN2.timeToSolution + distributed_data_RUN3.timeToSolution)/3);
            var averageDistributedGbest = Math.round((distributed_data_RUN1.finalGroupBest + distributed_data_RUN2.finalGroupBest + distributed_data_RUN3.finalGroupBest)/3);

            outputString = outputString + averageDistributedIterations + "," + averageDistributedTTS + "," + averageDistributedGbest + "\n";
        }
    }
}

fs.writeFile('./Results/' + functionName + '_FINAL.csv', outputString, function (err) {
    if (err) return console.log(err);
    console.log('Done');
});