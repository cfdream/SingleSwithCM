%CDF : #flow duration in 30 seconds, <src>
method1 = load('C:\workspace\projects\eclipse\PacketLoss\data\changeLossRate\method1');
method2_1k = load('C:\workspace\projects\eclipse\PacketLoss\data\changeLossRate\method2_1k');
method2_10k = load('C:\workspace\projects\eclipse\PacketLoss\data\changeLossRate\method2_10k');
method2_100 = load('C:\workspace\projects\eclipse\PacketLoss\data\changeLossRate\method2_100');
for i =1:10
    method1(i,5)=2*(1-method1(i,1))*method1(i,2)/((1-method1(i,1)) + method1(i,2))
    method2_1k(i,5)=2*(1-method2_1k(i,1))*method2_1k(i,2)/((1-method2_1k(i,1)) + method2_1k(i,2))
    method2_10k(i,5)=2*(1-method2_10k(i,1))*method2_10k(i,2)/((1-method2_10k(i,1)) + method2_10k(i,2))
    method2_100(i,5)=2*(1-method2_100(i,1))*method2_100(i,2)/((1-method2_100(i,1)) + method2_100(i,2))
end

figure
hold on;
x=1:1:10
plot(x, method1(:,5)', '-k*')
plot(x, method2_100(:,5)', '-r^')
plot(x, method2_1k(:,5)', '-go')
plot(x, method2_10k(:,5)', '-b+')

legend('{\itX}=0', '{\itX}=100', '{\itX}=1k', '{\itX}=10k');
set(gca, 'FontSize', 20, 'XTick', [2:2:10], 'xticklabel', [0.1:0.1:0.5]);
title('accumulated volume {\itX} vs. F-score')
xlabel('loss rate threshold')
ylabel('F-score')
xlim([0.9,10.1])
ylim([0.75, 0.92])
box on;
hold off;