%no limit of hashtable size
figure
normal=[0.3927297279656151,0.43707255281187896,0.1656248481575056];
linear_byte=[0.9355804882253208,0.0031519433022424207, 0.6565401724019198];
linear_packet=[0.6164163471845949,0.010837061154369759,0.4452072320612534];
log=[0.9433850655308879 0.0032351807556011913 0.6598432018491063];
Polynomial=[0.6551895879106516 0.0075977132350325315 0.481729192863667];
exponential=[0.4003676030369248 0.1386706735911284 0.3797790610733555];
std_normal=[0.12774450459045503,0.008930810836910143,0.004007127201515078];
std_linear_byte=[0.0031808184281565547,0.001030673659210401,0.004887432700710774];
std_linear_packet=[0.04001024237056504,0.0021325176946342546,0.0025895975164054712];
std_log=[0.0024748210556042563 6.921119262016754E-4 0.005325822691464453];
std_polynominal=[0.03531361209442094 0.0017723634334378309 0.0029525542500940323];
std_exponential=[0.09702748432437107 0.005821478803914358 0.001975078402619852];
hb = bar([1 2 3],[normal' linear_byte' log' Polynomial' exponential']);
legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
set(gca, 'FontSize', 12, 'XTick', [1 2 3], 'xticklabel', {'memory inflation', 'false negative', 'accuracy'});
ylabel('Relative percentage')

errbar=[std_normal; std_linear_byte; std_log; std_polynominal; std_exponential];
yd = [normal' linear_byte' log' Polynomial' exponential']';
hold on
for k1 = 1:5
    errorbar([1:3]+.15*(k1-3),  yd(k1,:),  errbar(k1,:), '.k', 'LineWidth',2)
end
hold off

%size of hash table: 10007
figure
normal=[0.4347659801922905 0.16734513204404713];
linear_byte=[0.044265681845994304 0.4163087899177092];
log=[0.04824840598240313 0.38735910907600557];
Polynomial=[0.028424418615617474 0.43098846906574695];
exponential=[0.15728213027906518 0.36487466600721086];
std_normal=[0.008256987442062267 0.00676309448281984];
std_linear_byte=[0.00420224953464288 0.0042156189428268695];
std_log=[0.003941062078666947 0.00484157337423277];
std_polynominal=[0.003262968757166595 0.009867289743455231];
std_exponential=[0.008668916114364959 0.00950776427792296];
hb = bar([1 2],[normal' linear_byte' log' Polynomial' exponential']);
legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
set(gca, 'FontSize', 22, 'XTick', [1 2], 'xticklabel', {'false negative', 'accuracy'});
%ylabel('Relative percentage')

errbar=[std_normal; std_linear_byte; std_log; std_polynominal; std_exponential];
yd = [normal' linear_byte' log' Polynomial' exponential']';
hold on
for k1 = 1:5
    errorbar([1:2]+.15*(k1-3),  yd(k1,:),  errbar(k1,:), '.k', 'LineWidth',2)
end
hold off
%size of hash table: 104743
figure
normal=[0.4315198805990133 0.16807775870676453];
linear_byte=[0.007889219964568334 0.6282661562050615];
log=[0.00861041278588809 0.6270198256113931];
Polynomial=[0.009033698619970184 0.48104507500699806];
exponential=[0.13792743299228252 0.3837386442044899];
std_normal=[0.009207779019821142 0.00773176096451279];
std_linear_byte=[0.00420224953464288 0.0042156189428268695];
std_log=[0.003941062078666947 0.00484157337423277];
std_polynominal=[0.0018328150167368482 0.009377644001543036];
std_exponential=[0.008070324036357782 0.00947604803745172];
hb = bar([1 2],[normal' linear_byte' log' Polynomial' exponential']);
legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
set(gca, 'FontSize', 22, 'XTick', [1 2], 'xticklabel', {'false negative', 'accuracy'});
%ylabel('Relative percentage')

errbar=[std_normal; std_linear_byte; std_log; std_polynominal; std_exponential];
yd = [normal' linear_byte' log' Polynomial' exponential']';
hold on
for k1 = 1:5
    errorbar([1:2]+.15*(k1-3),  yd(k1,:),  errbar(k1,:), '.k', 'LineWidth',2)
end
