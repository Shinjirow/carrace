make cai; #ここに各自のコンパイルするときのコマンドを書いてね　ぼくはこれでコンパイルする

for i in `seq 1 50`;
do
  echo "$i"
  make stats > null 2> null; # ここに各自のstatsを取るときのコマンドを書いてね ぼくはこれでstatsが動く
done;

python analyze.py < results.txt

rm results.txt
