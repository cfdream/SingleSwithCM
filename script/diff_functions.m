figure
x=0:0.001:0.2;
%loss volume, p0=2.2e-6
%linearY=6.67e-5*x+2.2e-6;  
%polynomialY=2.9629564e-13*x.^3+2.2e-6;
%logY=0.104*log(x+1);
%log10Y=0.2395*log(x+1)/log(10);
%expY=2.2e-6*exp(8.6847e-4*x)

%loss volume, p0=4e-5
%linearY=0.000066664*x + 4e-5;
%polynomialY=2.962844444e-13*x.^3 + 4e-5;
%logY=0.104*log(x+1);
%expY=4e-5*exp(6.7510874e-4*x);

%loss rate, p0=2e-4
linearY=4.999*x + 2e-4;
polynomialY=124.975*x.^3 + 2e-4;
logY=0.2181*log(485.1930*x+1.0009);
expY=2e-4*exp(42.58596596*x);

hold on;
%plot(x,linearY, 'b', 'LineWidth', 2)
%plot(x,logY, 'g', 'LineWidth', 2)
%plot(x,polynomialY, 'Color',  [1 0.5 0.2], 'LineWidth', 2)
%plot(x,expY, 'Color', [0.8 ,0, 0], 'LineWidth', 2)

plot(x,linearY, 'm', 'LineWidth', 2)
plot(x,logY, 'b', 'LineWidth', 2)
plot(x,polynomialY, 'g', 'LineWidth', 2)
plot(x,expY, 'r', 'LineWidth', 2)

legend('Linear', 'Log', 'Polynomial', 'Exponential')
legend('location', 'northwest');
legend('boxoff');
axis([0,0.2,0,1])
xlabel('Lost Rate', 'FontSize', 22)
ylabel('Byte Sampling Probability', 'FontSize', 22)
set(gca, 'FontSize', 22)
box on;
