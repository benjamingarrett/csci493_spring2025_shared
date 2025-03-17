import json
import matplotlib.pyplot as plt
import subprocess

h = open("spirals.json", "r")
raw_text = h.read()
h.close()
json_struct = json.loads(raw_text)
curves = json_struct['curves']

fig = plt.figure()
ax = fig.add_subplot(111)
for curve in curves:
  x = [pt[0] for pt in curve['points']]
  y = [pt[1] for pt in curve['points']]
  ax.plot(x, y)
plt.show()

