import sys
from datetime import datetime

import pandas as pd
import plotly.graph_objects as go
import plotly.express as px





def plot_percentile_timestamps( name:str,data_percentile:pd.DataFrame):
    fig = go.Figure()
    timestamps = list(map(lambda timestamp: datetime.fromtimestamp(timestamp).strftime("%H:%M:%S"), data_percentile["Timestamp"]))

    for percentile in filter(lambda  c: c != "Timestamp",data_percentile.columns):
        fig.add_trace(go.Scatter(x=timestamps, y=data_percentile[percentile], mode='lines', name=percentile))

    fig.update_layout(title=f"{name} percentiles",
                      xaxis_title="Timestamp",
                      yaxis_title='Response Time',
                      xaxis=dict(
                          tickmode='linear',
                          tick0=1,
                          dtick=15
                      )
                      )
    fig.show()

def plot_percentile_other( name:str,data_percentile:pd.DataFrame, x_axis):
    fig = go.Figure()
    for percentile in filter(lambda  c: c != x_axis,data_percentile.columns):
        fig.add_trace(go.Scatter(x=data_percentile[x_axis], y=data_percentile[percentile], mode='lines', name=percentile))

    fig.update_layout(title=f"{name} percentiles",
                      xaxis_title=x_axis,
                      yaxis_title='Response Time',
                      xaxis=dict(
                          tickmode='linear',
                          tick0=5,
                          dtick=5
                      )
                      )
    fig.show()

def plot_failures(name:str,data_failures:pd.DataFrame):
    fig = go.Figure()
    timestamps = list(
        map(lambda timestamp: datetime.fromtimestamp(timestamp).strftime("%H:%M:%S"), data_failures["Timestamp"]))

    fig.add_trace(go.Scatter(x=timestamps, y=data_failures["Total Failure Count"], mode='lines'))

    fig.update_layout(title=f"{name} Failures",
                      xaxis_title='Timestamp',
                      yaxis_title='Failures',
                      xaxis=dict(
                          tickmode='linear',
                          tick0=1,
                          dtick=15
                      )
                      )
    fig.show()

def plot_distribution(name:str,data_dist:pd.DataFrame):
    fig = px.histogram(data_dist, x="Total Max Response Time")
    fig.update_layout(title=f"{name} Response time distribution")
    fig.show()

def generate_report(csv_file:str) :
    data = pd.read_csv(f"{csv_file}")
    data = data.loc[data["User Count"] > 0]
    names = data["Name"].unique()

    for name in names:
        _data = data.loc[data["Name"] == name]
        data_percentile_timestamp = _data[["Timestamp", "Total Max Response Time", "50%","66%","75%","80%","90%","95%","98%","99%","99.9%","99.99%","100%"]]
        data_percentile_user_count = _data[
            ["User Count", "Total Max Response Time", "50%", "66%", "75%", "80%", "90%", "95%", "98%", "99%", "99.9%",
             "99.99%", "100%"]]
        data_failures = _data[["Timestamp", "Total Failure Count"]]
        data_dist = _data[["Total Max Response Time"]]

        plot_percentile_timestamps(name, data_percentile_timestamp)
        plot_percentile_other(name, data_percentile_user_count, "User Count")
        plot_failures(name, data_failures)
        plot_distribution(name, data_dist)



if __name__ == "__main__":
    csv_file = sys.argv[1]
    html_file_name = sys.argv[2]

    generate_report(csv_file)
