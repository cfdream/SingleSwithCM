%CDF : #flow duration in 30 seconds, <src>
method0 = load('C:\workspace\projects\eclipse\PacketLoss\data\difHashTableSize_threshold_0.2_thresholdVolume_1000\diffHashTable_method0');
method1 = load('C:\workspace\projects\eclipse\PacketLoss\data\difHashTableSize_threshold_0.2_thresholdVolume_1000\diffHashTable_method1');
%method2 = load('C:\workspace\projects\eclipse\PacketLoss\data\difHashTableSize_threshold_0.2_thresholdVolume_1000\diffHashTable_method2');
%method3 = load('C:\workspace\projects\eclipse\PacketLoss\data\difHashTableSize_threshold_0.2_thresholdVolume_1000\diffHashTable_method3');
%method4 = load('C:\workspace\projects\eclipse\PacketLoss\data\difHashTableSize_threshold_0.2_thresholdVolume_1000\diffHashTable_method4');

for i =1:4
    method0(i,5)=2*(1-method0(i,1))*method0(i,2)/((1-method0(i,1)) + method0(i,2))
    method1(i,5)=2*(1-method1(i,1))*method1(i,2)/((1-method1(i,1)) + method1(i,2))
    %method2(i,5)=2*(1-method2(i,1))*method2(i,2)/((1-method2(i,1)) + method2(i,2))
    %method3(i,5)=2*(1-method3(i,1))*method3(i,2)/((1-method3(i,1)) + method3(i,2))
    %method4(i,5)=2*(1-method4(i,1))*method4(i,2)/((1-method4(i,1)) + method4(i,2))
end

figure
hold on;
x=1:1:4
plot(x, method0(:,5)', '-r*')
plot(x, method1(:,5)', '-bo')
%plot(x, method2(:,5)', '-cv')
%plot(x, method3(:,5)', '-ro')
%plot(x, method4(:,5)', '-gx')

legend('method0', 'method1', 'method2,X>1k', 'method3', 'method4,X>1k');
set(gca, 'FontSize', 22, 'XTick', [1:1:4], 'xticklabel', {'1k', '10k', '100k', '1M'});
title('Replacement Mechanisms Comparison')
xlabel('Hashtable size (bytes)')
ylabel('F-score')
box on;
hold off;