
figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowLostVolume.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22, 'xtick', [100, 1000, 10000, 100000, 1000000, 10000000]);
title('CDF of lost volume for flows')
axis([64 10^7 0 1])
xlabel('lost volume(bytes)')
ylabel('CDF')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowLostRate.txt');
plot(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22, 'XTick', [0:0.2:1]);
title('CDF of lost rate for flows')
xlabel('lost rate')
ylabel('CDF')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowVolume_lossRate__first_30_seconds.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22, 'xtick', [100, 1000, 10000, 100000, 1000000, 10000000]);
title('flow volume vs. loss rate')
axis([64 10^7 0 1])
xlabel('flow volume(bytes)')
ylabel('loss rate')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowVolume_accuracy__method0_2e-4.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22, 'xtick', [100, 1000, 10000, 100000, 1000000, 10000000]);
title('flow volume vs. accuracy')
axis([20000 10^7 0 1])
xlabel('flow volume(bytes)')
ylabel('accuracy')