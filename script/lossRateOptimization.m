%CDF : #flow duration in 30 seconds, <src>
method2_0 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_0');
method2_100 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_100');
method2_1000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_1000');
method2_10000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_10000');
method2_100000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_100000');
method2_1000000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_1000000');
method2_5000000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffVolumeThreshold_method2\method2_5000000');
method2_0(:,1)'
method2_0(:,3)'
%false negative
figure
hold on;
x=1:1:4
errorbar(x, method2_0(:,1)', method2_0(:,3)', '-m>')
errorbar(x, method2_100(:,1)', method2_100(:,3)', '-cv')
errorbar(x, method2_1000(:,1)', method2_1000(:,3)', '-r^')
errorbar(x, method2_10000(:,1)', method2_10000(:,3)', '-gx')
errorbar(x, method2_100000(:,1)', method2_100000(:,3)', '-b+')
errorbar(x, method2_1000000(:,1)', method2_1000000(:,3)', '-k*')
%errorbar(x, method2_5000000(:,1)', method2_5000000(:,3)', '-bo')

legend('X=0', 'X=100', 'X=10^3', 'X=10^4', 'X=10^5', 'X=10^6');
set(gca, 'FontSize', 16, 'XTick', [1:1:4], 'xticklabel', [0.1:0.1:0.4]);
title('method2 X(bytes) vs. false positive')
xlabel('loss rate threshold')
ylabel('false negative')
xlim([0.9,4.1])
box on;
hold off;

%accuracy
figure
hold on;
x=1:1:4
errorbar(x, method2_0(:,2)', method2_0(:,4)', '-m>')
errorbar(x, method2_100(:,2)', method2_100(:,4)', '-cv')
errorbar(x, method2_1000(:,2)', method2_1000(:,4)', '-r^')
errorbar(x, method2_10000(:,2)', method2_10000(:,4)', '-gx')
errorbar(x, method2_100000(:,2)', method2_100000(:,4)', '-b+')
errorbar(x, method2_1000000(:,2)', method2_1000000(:,4)', '-k+')
%errorbar(x, method2_5000000(:,2)', method2_5000000(:,4)', '-bo')

legend('X=0', 'X=100', 'X=10^3', 'X=10^4', 'X=10^5', 'X=10^6');
set(gca, 'FontSize', 16, 'XTick', [1:1:4], 'xticklabel', [0.1:0.1:0.4]);
title('method2 X(bytes) vs. accuracy')
xlabel('loss rate threshold')
ylabel('accuracy')
xlim([0.9,4.1])
box on;
hold off;
