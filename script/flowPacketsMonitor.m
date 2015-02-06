figure
lost = load('C:\workspace\projects\eclipse\PacketLoss\data\1176455053_lost.txt');
plot(lost(:,1),lost(:,2), 'r-', 'LineWidth', 1)
hold on
normal = load('C:\workspace\projects\eclipse\PacketLoss\data\1176455053_normal.txt');
plot(normal(:,1),normal(:,2), 'g-', 'LineWidth', 1)