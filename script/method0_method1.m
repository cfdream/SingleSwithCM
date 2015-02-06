
function method0_method1()
%CDF : #flow duration in 30 seconds, <src>
method0_3 = load('C:\workspace\projects\eclipse\PacketLoss\data\method1_method0\method0_prob_2e-3_limitSendToController_final.txt');
method0_4 = load('C:\workspace\projects\eclipse\PacketLoss\data\method1_method0\method0_prob_2e-4_limitSendToController_final.txt');
method1_3 = load('C:\workspace\projects\eclipse\PacketLoss\data\method1_method0\method1_prob_2e-3_limitSendToController_final.txt');
%false negative
figure
hold on;
for i=1:4
    method0_4(i,4)=ci95(method0_4(i,4));
    method0_3(i,4)=ci95(method0_3(i,4));
    method1_3(i,4)=ci95(method1_3(i,4));
    
    method0_4(i,5)=ci95(method0_4(i,5));
    method0_3(i,5)=ci95(method0_3(i,5));
    method1_3(i,5)=ci95(method1_3(i,5));
    
    method0_4(i,6)=ci95(method0_4(i,6));
    method0_3(i,6)=ci95(method0_3(i,6));
    method1_3(i,6)=ci95(method1_3(i,6));
end
method0_4(:,3)
method0_3(:,3)
method1_3(:,3)

x=1:1:4;
errorbar(x, method0_4(:,2)', method0_4(:,5)', '-rv', 'linewidth', 2)
errorbar(x, method0_3(:,2)', method0_3(:,5)', '-g>', 'linewidth', 2)
errorbar(x, method1_3(:,2)', method1_3(:,5)', '-b^', 'linewidth', 2)

set(gca, 'FontSize', 20, 'XTick', [1:1:4], 'xticklabel', [0.1:0.1:0.4]);
h_legend=legend('Traditional, p_0=2e-4', 'Traditional, p_0=2e-3', 'Replace Mechanism');
legend('Location', 'northwest');
legend('boxoff');
%title('different methods comparison')
xlabel('loss rate threshold (\itT)')
ylabel('false negative')
xlim([0.9,4.1])
ylim([-.05,.35])
box on;
hold off;

%accuracy
figure
hold on;
x=1:1:4
errorbar(x, method0_4(:,3)', method0_4(:,6)', '-rv', 'linewidth', 2)
errorbar(x, method0_3(:,3)', method0_3(:,6)', '-g>', 'linewidth', 2)
errorbar(x, method1_3(:,3)', method1_3(:,6)', '-b^', 'linewidth', 2)

set(gca, 'FontSize', 20, 'XTick', [1:1:4], 'xticklabel', [0.1:0.1:0.4]);
%title('different methods comparison')
legend('Traditional, p_0=2e-4', 'Traditional, p_0=2e-3', 'Replace Mechanism');
legend('Location', 'southwest');
legend('boxoff');
xlabel('loss rate threshold (\itT)')
ylabel('accuracy')
xlim([0.9,4.1])
ylim([0.7,1.05])
box on;
hold off;

%false positive
figure
hold on;
x=1:1:4
errorbar(x, method0_4(:,1)', method0_4(:,4)', '-rv', 'linewidth', 2)
errorbar(x, method0_3(:,1)', method0_3(:,4)', '-g>', 'linewidth', 2)
errorbar(x, method1_3(:,1)', method1_3(:,4)', '-b^', 'linewidth', 2)

set(gca, 'FontSize', 20, 'XTick', [1:1:4], 'xticklabel', [0.1:0.1:0.4]);
%title('different methods comparison')
legend('Traditional, p_0=2e-4', 'Traditional, p_0=2e-3', 'Replace Mechanism');
legend('Location', 'northwest');
legend('boxoff');
xlabel('loss rate threshold (\itT)')
ylabel('false positive')
xlim([0.9,4.1])
%ylim([0.7,1.05])
box on;
hold off;

end

function error = ci95(d)  %d: standard deviation
    %p95 = 1.96;         % normal distribution
    p95 = 2.093;		% t distribution	t distribution degree of freedom- 19
	N = 20;
	s2 = d*d * N / (N-1);
	sm = sqrt(s2)/sqrt(N);
    error = sm*p95;
end
