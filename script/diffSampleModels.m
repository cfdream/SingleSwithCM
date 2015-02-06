
%start sampling prob: 4e-5
models = load('C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\diffModels_4e-5.txt');

%false negative
figure
fn = models(:,1)';
b=fn;
hold on;
color=[1;2;3;4;5];
for i=1:length(fn)
    b(i)=bar(i, fn(i));
    ch = get(b(i),'children');
    set(ch,'FaceVertexCData',color(i));
end
%b=bar(models(:,1)');
%ch = get(b,'children');
%set(ch,'FaceVertexCData',[1;2;3;4;5])
%legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
%legend('Location', 'northeast');
%legend('boxoff');
errorbar(fn, models(:,3), '.k', 'linewidth', 2);
set(gca, 'FontSize', 22, 'XTick', []);
ylabel('False negative')
box on;
%ylabel('Relative percentage')


%accuracy
figure
accuracy = models(:,2)';
b=accuracy;
hold on;
color=[1;2;3;4;5];
for i=1:length(accuracy)
    b(i)=bar(i, accuracy(i));
    ch = get(b(i),'children');
    set(ch,'FaceVertexCData',color(i));
end
legend('Traditional', 'Linear', 'Log', 'Polynomial', 'Exponential');
legend('Location', 'northwest');
legend('boxoff');
errorbar(accuracy, models(:,4), '.k', 'linewidth', 2);
set(gca, 'FontSize', 22, 'XTick', []);
ylabel('Accuracy')
ylim([0.75,1.05])
box on;


