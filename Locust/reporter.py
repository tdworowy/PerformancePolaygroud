import sys

import mpld3
import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.figure import Figure
from matplotlib.axes import  Axes



def plot_percentile(i:int, name:str,data_percentile:pd.DataFrame,axis:Axes):
    plot = data_percentile.plot(x="Timestamp", ax=axis[i, 0])

    plot.set_facecolor('#B0C4DE')
    plot.set_title(f"{name} percentiles")

    box = plot.get_position()
    plot.set_position([box.x0, box.y0, box.width * 0.8, box.height])
    plot.legend(loc='center left', bbox_to_anchor=(1, 0.5))

def plot_failures(i:int, name:str,data_failures:pd.DataFrame,axis:Axes):
    plot = data_failures.plot(x="Timestamp", ax=axis[i, 1])

    plot.set_facecolor('#B0C4DE')
    plot.set_title(f"{name} failures")

    box = plot.get_position()
    plot.set_position([box.x0, box.y0, box.width * 0.8, box.height])
    plot.legend(loc='center left', bbox_to_anchor=(1, 0.5))

def plot_distribution(i:int, name:str,data_dist:pd.DataFrame,axis:Axes):
    plot = data_dist.plot.hist(ax=axis[i, 2], density=True,)

    plot.set_facecolor('#B0C4DE')
    plot.set_title(f"{name} Total Max Response Time")

    box = plot.get_position()
    plot.set_position([box.x0, box.y0, box.width * 0.8, box.height])
    plot.legend(loc='center left', bbox_to_anchor=(1, 0.5))

def generate_report(csv_file_prefix:str) -> Figure:
    data = pd.read_csv(f"{csv_file_prefix}.csv_stats_history.csv")
    names = data["Name"].unique()

    figure, axis = plt.subplots(nrows=len(names), ncols=3, constrained_layout=True)

    x, y = figure.get_size_inches()
    figure.set_figwidth(x * 5)
    figure.set_figheight(y * 5)

    figure.set_facecolor('#6A5ACD')
    figure.tight_layout(pad=15)

    for i,name in enumerate(names):
        _data = data.loc[data["Name"] == name]
        data_percentile = _data[["Timestamp", "Total Max Response Time", "50%","66%","75%","80%","90%","95%","98%","99%","99.9%","99.99%","100%"]]
        data_failures = _data[["Timestamp", "Total Failure Count"]]
        data_dist = _data[["Total Max Response Time"]]

        plot_percentile(i,name, data_percentile,axis)
        plot_failures(i,name, data_failures,axis)
        plot_distribution(i,name, data_dist,axis)

    return figure

def generate_report_interactive(fig:Figure, file_name:str):
      mpld3.save_html(fig, file_name)

if __name__ == "__main__":
    csv_prefix = sys.argv[1]
    html_file_name = sys.argv[2]

    fig = generate_report(csv_prefix)
    generate_report_interactive(fig, html_file_name)
