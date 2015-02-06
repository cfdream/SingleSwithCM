function diffSampleModelsDiffVolumeThreshold()
    volume0 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\volume0');
    volume100 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\volume100');
    volume1000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\volume1000');
    volume10000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\volume10000');
    volume100000 = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\volume100000');

    exp(1,1)=0;
    pol(1,1)=0;
    log(1,1)=0;
    lin(1,1)=0;
    tra(1,1)=0;
    for i=1:6
        ith=1;
        exp(1,i)=volume0(ith,i);
        exp(2,i)=volume100(ith,i);
        exp(3,i)=volume1000(ith,i);
        exp(4,i)=volume10000(ith,i);
        exp(5,i)=volume100000(ith,i);
        ith=ith+1;
        pol(1,i)=volume0(ith,i);
        pol(2,i)=volume100(ith,i);
        pol(3,i)=volume1000(ith,i);
        pol(4,i)=volume10000(ith,i);
        pol(5,i)=volume100000(ith,i);
        ith=ith+1;
        log(1,i)=volume0(ith,i);
        log(2,i)=volume100(ith,i);
        log(3,i)=volume1000(ith,i);
        log(4,i)=volume10000(ith,i);
        log(5,i)=volume100000(ith,i);
        ith=ith+1;
        lin(1,i)=volume0(ith,i);
        lin(2,i)=volume100(ith,i);
        lin(3,i)=volume1000(ith,i);
        lin(4,i)=volume10000(ith,i);
        lin(5,i)=volume100000(ith,i);
        ith=ith+1;
        tra(1,i)=volume0(ith,i);
        tra(2,i)=volume100(ith,i);
        tra(3,i)=volume1000(ith,i);
        tra(4,i)=volume10000(ith,i);
        tra(5,i)=volume100000(ith,i);    
    end
    for i=4:6
        exp(1,i)=ci95(exp(1,i));
        exp(2,i)=ci95(exp(2,i));
        exp(3,i)=ci95(exp(3,i));
        exp(4,i)=ci95(exp(4,i));
        exp(5,i)=ci95(exp(5,i));
        
        pol(1,i)=ci95(pol(1,i));
        pol(2,i)=ci95(pol(2,i));
        pol(3,i)=ci95(pol(3,i));
        pol(4,i)=ci95(pol(4,i));
        pol(5,i)=ci95(pol(5,i));

        log(1,i)=ci95(log(1,i));
        log(2,i)=ci95(log(2,i));
        log(3,i)=ci95(log(3,i));
        log(4,i)=ci95(log(4,i));
        log(5,i)=ci95(log(5,i));

        lin(1,i)=ci95(lin(1,i));
        lin(2,i)=ci95(lin(2,i));
        lin(3,i)=ci95(lin(3,i));
        lin(4,i)=ci95(lin(4,i));
        lin(5,i)=ci95(lin(5,i));

        tra(1,i)=ci95(tra(1,i));
        tra(2,i)=ci95(tra(2,i));
        tra(3,i)=ci95(tra(3,i));
        tra(4,i)=ci95(tra(4,i));
        tra(5,i)=ci95(tra(5,i));   
    end

    %false negative
    figure
    hold on;
    x=1:1:5;
    errorbar(x, tra(:,2)', tra(:,5)', '-co', 'linewidth', 2)
    errorbar(x, lin(:,2)', lin(:,5)', '-m*', 'linewidth', 2)
    errorbar(x, log(:,2)', log(:,5)', '-b^', 'linewidth', 2)
    %errorbar(x, pol(:,2)', pol(:,5)', '-g>', 'linewidth', 2)
    errorbar(x, exp(:,2)', exp(:,5)', '-rv', 'linewidth', 2)

    set(gca, 'FontSize', 20, 'XTick', 1:1:5, 'xticklabel', [0, 10^2, 10^3, 10^4, 10^5]);
    legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
    %legend('Location', 'north');
    legend('boxoff');
    %title('different methods comparison')
    xlabel('accumulated volume (bytes)')
    ylabel('false negative')
    xlim([0.95, 5.05])
    %ylim([0,.07])
    box on;
    hold off;

    %accuracy
    figure
    hold on;
    x=1:1:5;
    errorbar(x, tra(:,3)', tra(:,6)', '-co', 'linewidth', 2)
    errorbar(x, lin(:,3)', lin(:,6)', '-m*', 'linewidth', 2)
    errorbar(x, log(:,3)', log(:,6)', '-b^', 'linewidth', 2)
    %errorbar(x, pol(:,3)', pol(:,6)', '-g>', 'linewidth', 2)
    errorbar(x, exp(:,3)', exp(:,6)', '-rv', 'linewidth', 2)

    set(gca, 'FontSize', 20, 'XTick', 1:1:5, 'xticklabel', [0, 10^2, 10^3, 10^4, 10^5]);
    legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
    %legend('Location', 'southwest');
    legend('boxoff');
    xlabel('accumulated volume (bytes)')
    ylabel('accuracy')
    xlim([0.95, 5.05])
    %ylim([0.83, 0.97])
    box on;
    hold off;

    %false positive
    figure
    hold on;
    x=1:1:5;
    errorbar(x, tra(:,1)', tra(:,4)', '-co', 'linewidth', 2)
    errorbar(x, lin(:,1)', lin(:,4)', '-m*', 'linewidth', 2)
    errorbar(x, log(:,1)', log(:,4)', '-b^', 'linewidth', 2)
    %errorbar(x, pol(:,1)', pol(:,4)', '-g>', 'linewidth', 2)
    errorbar(x, exp(:,1)', exp(:,4)', '-rv', 'linewidth', 2)

    set(gca, 'FontSize', 20, 'XTick', 1:1:5, 'xticklabel', [0, 10^2, 10^3, 10^4, 10^5]);
    legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
    %legend('Location', 'southwest');
    legend('boxoff');
    xlabel('accumulated volume (bytes)')
    ylabel('false positive')
    xlim([0.95, 5.05])
    %ylim([0.83, 0.97])
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