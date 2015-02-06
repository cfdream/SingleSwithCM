%CDF : #flow duration in 30 seconds, <src>
method2_0 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffWaitTime\method3_0');
method2_100 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffWaitTime\method3_100');
method2_10000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffWaitTime\method3_10000');
method2_1000000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffWaitTime\method3_1000000');
method2_0(:,1)
%false negative
figure
hold on;
x=1:1:4
errorbar(x, method2_0(:,1)', method2_0(:,3)', '-m>')
errorbar(x, method2_100(:,1)', method2_100(:,3)', '-rv')
errorbar(x, method2_10000(:,1)', method2_10000(:,3)', '-gx')
errorbar(x, method2_1000000(:,1)', method2_1000000(:,3)', '-b+')

legend('Y=0', 'Y=1^2', 'Y=10^4', 'Y=10^6');
set(gca, 'FontSize', 16, 'xticklabel', [0.1:0.1:0.4]);
title('method3 Y(us) vs. false positive')
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
errorbar(x, method2_100(:,2)', method2_100(:,4)', '-rv')
errorbar(x, method2_10000(:,2)', method2_10000(:,4)', '-gx')
errorbar(x, method2_1000000(:,2)', method2_1000000(:,4)', '-b+')

legend('Y=0', 'Y=1^2', 'Y=10^4', 'Y=10^6');
set(gca, 'FontSize', 16, 'xticklabel', [0.1:0.1:0.4]);
title('method3 Y(us) vs. accuracy')
xlabel('loss rate threshold')
ylabel('accuracy')
xlim([0.9,4.1])
%ylim([0.975,0.99])
box on;
hold off;
