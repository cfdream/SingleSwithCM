
def extract_data(fileName):
	file=open(fileName);
	lines=file.readlines();
	itemOver=0;
	for line in lines:
		items = line.split()
		if len(items) == 4:
			temp=items[1].split(':');
			falsePositive = (float)(temp[1])
			temp=items[2].split(':')
			falseNegative = (float)(temp[1])
			temp=items[3].split(':')
			accuracy = (float)(temp[1])
		elif len(items) == 3:
			if len(items[2].split(':')) == 2:
				continue
			if len(line.split(':')) == 1:
				sdn_fp=(float)(items[0])
				sde_fn=(float)(items[1])
				sde_accuracy = (float)(items[2])
				itemOver=1
		if itemOver == 1:
			itemOver=0
			print (falsePositive, falseNegative, accuracy, sdn_fp, sde_fn, sde_accuracy)

extract_data("C:\workspace\projects\eclipse\PacketLoss\data\diffSampleModels\intervalResults_DiffModel__prob_2.0E-4_volumeThreshold_100000.txt")