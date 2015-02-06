%CDF : #flow duration in 30 seconds, <src>
figure 
matrix = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowDuration.txt');
semilogx(matrix(:,1),matrix(:,2), 'r-', 'LineWidth', 2)
title('CDF of flow duration in 30s (<src> tuple)')
xlabel('duration (microseconds, 1s=1000,000 us)')
ylabel('CDF')

%CDF : #pkg for flows in 30 seconds, <src, dst>
%figure 
%matrix = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowPacketNumDistributionInOneInterval_30s_src_dst.txt');
%semilogx(matrix(:,1),matrix(:,2), 'r-*')
%title('CDF of number of packet for flows in 30s (<src, dst> tuple)')
%xlabel('packet number of one flow')
%ylabel('CDF')

%CDF : #pkg for flows in 30 seconds, <src>
%figure
%matrix = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowPacketNumDistributionInOneInterval_30s_src.txt');
%semilogx(matrix(:,1),matrix(:,2), 'r-', 'LineWidth', 2)
%title('CDF of number of packet for flows in 30s (<src> tuple)')
%xlabel('packet number of one flow')
%ylabel('CDF')

%CDF: volume of all flows and flow (#packet > 1000) in 30 seconds, <src>
figure
matrix = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowVolumeDistributionInOneInterval.txt');
semilogx(matrix(:,1),matrix(:,2), 'r-', 'LineWidth', 3)
title('CDF of flow volume for all flows in 30s (<src> tuple)')
xlabel('flow volume')
ylabel('CDF')
%legend('all flows', 'flows(#package > 1000)');
%legend('Location', 'northwest')
%legend('boxoff');
axis([64 10000000 0 1]);
%text(10000, 0.5,'\leftarrow all flows');
%text(1000000, 0.5,' \leftarrow flows(#package > 1000)');

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowAvgPacketLength.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 3)
title('CDF of average packet length for flows')
axis([64 10000 0 1])
xlabel('packet length')
ylabel('CDF')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowLostVolume.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22);
title('CDF of lost volume for flows')
axis([64 10^7 0 1])
xlabel('lost volume')
ylabel('CDF')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowLostRate.txt');
plot(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22);
title('CDF of lost rate for flows')
xlabel('lost rate')
ylabel('CDF')

figure
matrix1k = load('C:\workspace\projects\eclipse\PacketLoss\data\analyzeFlowVolume_lossRate__first_30_seconds.txt');
semilogx(matrix1k(:,1),matrix1k(:,2), 'r-', 'LineWidth', 2)
set(gca, 'FontSize', 22);
title('flow volume vs. loss rate')
axis([64 10^7 0 1])
xlabel('flow volume')
ylabel('loss rate')


figure 
%a=[0.3927297279656151,0.43707255281187896,0.1656248481575056; 0.6164163471845949,0.010837061154369759,0.4452072320612534];
%b=[0.6164163471845949 0.010837061154369759 0.4452072320612534];
a=[0.3927297279656151,0.6164163471845949;
   0.43707255281187896,0.010837061154369759;
   0.1656248481575056,0.4452072320612534];
new_a=[0.3927297279656151,0.9355804882253208;
   0.43707255281187896,0.0031519433022424207;
   0.1656248481575056,0.6565401724019198];
e=[0.12774450459045503,0.0031808184281565547;
    0.008930810836910143,0.001030673659210401;
    0.004007127201515078,0.004887432700710774];
bar(a)
legend('Normal Sample and Hold', 'Linear Increasing Sample and Hold')
set(gca, 'xticklabel', {'memory inflation', 'false negative', 'accuracy'});
