using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using System.Linq;

public class Program
{
    public static void Main()
    {
        Application.EnableVisualStyles();
        Application.SetCompatibleTextRenderingDefault(false);
        Application.Run(new Form1());
    }
}

public class Form1 : Form
{
    private Button button1;
    private Label label1;
    private Label label2;
    private Label label3;
    private RichTextBox richTextBox1;
    private RichTextBox richTextBox2;
    private RichTextBox richTextBox3;
    private TcpListener listener;
    private int index = 1;
    private List<List<int>> holdingTimestamp = new List<List<int>>();
    private int firstTimestamp = 0;
    private List<string> receivedMessage = new List<string>();
    private int timestampMessageCount = 0;
    private const int TotalMiddlewareCount = 5;
    private List<List<int>> localTimestamp = new List<List<int>>();
    private int finalLocalTimestamp = 0;
    private List<List<int>> finalTimestamps = new List<List<int>>();

    public Form1()
    {
        this.Text = "Middleware1";
        this.Size = new System.Drawing.Size(700, 300);

        button1 = new Button();
        button1.Size = new System.Drawing.Size(100, 50);
        button1.Location = new System.Drawing.Point(35, 15);
        button1.Text = "Send Message";
        button1.Click += new EventHandler(button1_Click);

        label1 = new Label();
        label1.Text = "Sent";
        label1.Location = new System.Drawing.Point(110, 95);
        label1.Size = new System.Drawing.Size(80, 20);

        label2 = new Label();
        label2.Text = "Received";
        label2.Location = new System.Drawing.Point(315, 95);
        label2.Size = new System.Drawing.Size(80, 20);

        label3 = new Label();
        label3.Text = "Ready";
        label3.Location = new System.Drawing.Point(540, 95);
        label3.Size = new System.Drawing.Size(80, 20);

        richTextBox1 = new RichTextBox();
        richTextBox1.Location = new System.Drawing.Point(35, 125);
        richTextBox1.Width = 175;
        richTextBox1.Height = 100;
        richTextBox1.Multiline = true;
        richTextBox1.ScrollBars = RichTextBoxScrollBars.Vertical;

        richTextBox2 = new RichTextBox();
        richTextBox2.Location = new System.Drawing.Point(255, 125);
        richTextBox2.Width = 175;
        richTextBox2.Height = 100;
        richTextBox2.Multiline = true;
        richTextBox2.ScrollBars = RichTextBoxScrollBars.Vertical;

        richTextBox3 = new RichTextBox();
        richTextBox3.Location = new System.Drawing.Point(475, 125);
        richTextBox3.Width = 175;
        richTextBox3.Height = 100;
        richTextBox3.Multiline = true;
        richTextBox3.ScrollBars = RichTextBoxScrollBars.Vertical;

        Controls.Add(button1);
        Controls.Add(label1);
        Controls.Add(label2);
        Controls.Add(label3);
        Controls.Add(richTextBox1);
        Controls.Add(richTextBox2);
        Controls.Add(richTextBox3);

        listener = new TcpListener(IPAddress.Any, 8082);
        listener.Start();
        ListenForClientsAsync();
    }

    private async void ListenForClientsAsync()
    {
        while (true)
        {
            TcpClient client = await listener.AcceptTcpClientAsync();
            ReadMessageAsync(client);
        }
    }

    private async void ReadMessageAsync(TcpClient client)
    {
        byte[] buffer = new byte[1024];
        await client.GetStream().ReadAsync(buffer, 0, buffer.Length);
        string message = Encoding.UTF8.GetString(buffer).Trim('\0');


        if (message.Contains("Middleware 1"))
        {
            firstTimestamp += 1;
            holdingTimestamp.Add(new List<int>() { firstTimestamp, 1 });
            localTimestamp.Add(new List<int> { firstTimestamp, 1 });
            richTextBox2.AppendText($"{message}\n");
            receivedMessage.Add(message);
            SendTimestamp(holdingTimestamp);
        }
        else if (message.Contains("Middleware 2"))
        {
            firstTimestamp += 1;
            holdingTimestamp.Add(new List<int>() { firstTimestamp, 2 });
            richTextBox2.AppendText($"{message}\n");
            receivedMessage.Add(message);
            SendTimestamp(holdingTimestamp);
        }
        else if (message.Contains("Middleware 3"))
        {
            firstTimestamp += 1;
            holdingTimestamp.Add(new List<int>() { firstTimestamp, 3 });
            richTextBox2.AppendText($"{message}\n");
            receivedMessage.Add(message);
            SendTimestamp(holdingTimestamp);
        }
        else if (message.Contains("Middleware 4"))
        {
            firstTimestamp += 1;
            holdingTimestamp.Add(new List<int>() { firstTimestamp, 4 });
            richTextBox2.AppendText($"{message}\n");
            receivedMessage.Add(message);
            SendTimestamp(holdingTimestamp);
        }
        else if (message.Contains("Middleware 5"))
        {
            firstTimestamp += 1;
            holdingTimestamp.Add(new List<int>() { firstTimestamp, 5 });
            richTextBox2.AppendText($"{message}\n");
            receivedMessage.Add(message);
            SendTimestamp(holdingTimestamp);
        }
        else if (message.StartsWith("Timestamp:"))
        {
            ExtractTimestampAndSource(message);
            if (finalTimestamps.Count == receivedMessage.Count)
            {
                PrintReadyMessage(finalTimestamps, receivedMessage);
            }
            return;
        }
        else if (message.StartsWith("FTS:"))
        {
            ExtractFinalTimestampAndSource(message);
            return;
        }
    }

    private async void SendTimestamp(List<List<int>> holdingTimestamp)
    {
        if (holdingTimestamp.Count >= 1)
        {
            if (holdingTimestamp[holdingTimestamp.Count - 1][1] == 1)
            {
                timestampMessageCount = localTimestamp.Count;
                var result = new List<int>();

                if (timestampMessageCount == TotalMiddlewareCount)
                {
                    // find max local timestamp
                    finalLocalTimestamp = GetMaxLocalTimestampAndClear(localTimestamp);
                    result.Add(finalLocalTimestamp);
                    result.Add(1);
                    finalTimestamps.Add(result);

                    //sent final local timestamp
                    TcpClient clientToM2 = new TcpClient("localhost", 8083);
                    byte[] data2 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);//FTS means final timestamp
                    await clientToM2.GetStream().WriteAsync(data2, 0, data2.Length);

                    TcpClient clientToM3 = new TcpClient("localhost", 8084);
                    byte[] data3 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
                    await clientToM3.GetStream().WriteAsync(data3, 0, data3.Length);

                    TcpClient clientToM4 = new TcpClient("localhost", 8085);
                    byte[] data4 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
                    await clientToM4.GetStream().WriteAsync(data4, 0, data4.Length);

                    TcpClient clientToM5 = new TcpClient("localhost", 8086);
                    byte[] data5 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
                    await clientToM5.GetStream().WriteAsync(data5, 0, data5.Length);

                }
                if (finalTimestamps.Count == receivedMessage.Count)
                {
                    PrintReadyMessage(finalTimestamps, receivedMessage);
                }
            }
            else if (holdingTimestamp[holdingTimestamp.Count - 1][1] == 2)
            {
                TcpClient clientToM2 = new TcpClient("localhost", 8083);
                byte[] data = Encoding.UTF8.GetBytes("Timestamp: " + holdingTimestamp[holdingTimestamp.Count - 1][0] + " From: " + holdingTimestamp[holdingTimestamp.Count - 1][1]);
                await clientToM2.GetStream().WriteAsync(data, 0, data.Length);
            }
            else if (holdingTimestamp[holdingTimestamp.Count - 1][1] == 3)
            {
                TcpClient clientToM3 = new TcpClient("localhost", 8084);
                byte[] data = Encoding.UTF8.GetBytes("Timestamp: " + holdingTimestamp[holdingTimestamp.Count - 1][0] + " From: " + holdingTimestamp[holdingTimestamp.Count - 1][1]);
                await clientToM3.GetStream().WriteAsync(data, 0, data.Length);
            }
            else if (holdingTimestamp[holdingTimestamp.Count - 1][1] == 4)
            {
                TcpClient clientToM4 = new TcpClient("localhost", 8085);
                byte[] data = Encoding.UTF8.GetBytes("Timestamp: " + holdingTimestamp[holdingTimestamp.Count - 1][0] + " From: " + holdingTimestamp[holdingTimestamp.Count - 1][1]);
                await clientToM4.GetStream().WriteAsync(data, 0, data.Length);
            }
            else if (holdingTimestamp[holdingTimestamp.Count - 1][1] == 5)
            {
                TcpClient clientToM5 = new TcpClient("localhost", 8086);
                byte[] data = Encoding.UTF8.GetBytes("Timestamp: " + holdingTimestamp[holdingTimestamp.Count - 1][0] + " From: " + holdingTimestamp[holdingTimestamp.Count - 1][1]);
                await clientToM5.GetStream().WriteAsync(data, 0, data.Length);
            }
        }
    }

    private async void button1_Click(object sender, EventArgs e)
    {
        TcpClient client = new TcpClient("localhost", 8081);
        byte[] data = Encoding.UTF8.GetBytes("Msg #" + index + " from Middleware 1");
        await client.GetStream().WriteAsync(data, 0, data.Length);
        richTextBox1.AppendText("Message " + index + " has been sent.\n");
        index += 1;
    }

    private async void ExtractTimestampAndSource(string message)
    {
        var regex = new Regex(@"Timestamp: (\d+) From: (\d+)");
        var match = regex.Match(message);

        if (match.Success)
        {
            int timestamp = int.Parse(match.Groups[1].Value);
            int source = int.Parse(match.Groups[2].Value);
            ReceiveTimestampMessageAndSendFinalTs(timestamp, source);
        }
    }

    private async void ReceiveTimestampMessageAndSendFinalTs(int timestamp, int source)
    {
        localTimestamp.Add(new List<int> { timestamp, source });
        timestampMessageCount = localTimestamp.Count;
        var result = new List<int>();

        if (timestampMessageCount == TotalMiddlewareCount)
        {
            finalLocalTimestamp = GetMaxLocalTimestampAndClear(localTimestamp);
            result.Add(finalLocalTimestamp);
            result.Add(1);
            finalTimestamps.Add(result);

            TcpClient clientToM2 = new TcpClient("localhost", 8083);
            byte[] data2 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
            await clientToM2.GetStream().WriteAsync(data2, 0, data2.Length);

            TcpClient clientToM3 = new TcpClient("localhost", 8084);
            byte[] data3 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
            await clientToM3.GetStream().WriteAsync(data3, 0, data3.Length);

            TcpClient clientToM4 = new TcpClient("localhost", 8085);
            byte[] data4 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
            await clientToM4.GetStream().WriteAsync(data4, 0, data4.Length);

            TcpClient clientToM5 = new TcpClient("localhost", 8086);
            byte[] data5 = Encoding.UTF8.GetBytes("FTS: " + finalLocalTimestamp + " From: " + 1);
            await clientToM5.GetStream().WriteAsync(data5, 0, data5.Length);

        }
    }

    private int GetMaxLocalTimestampAndClear(List<List<int>> localTimestamp)
    {
        int maxTimestamp = localTimestamp.Max(sublist => sublist[0]);
        localTimestamp.Clear();
        return maxTimestamp;
    }

    private void ExtractFinalTimestampAndSource(string message)
    {
        var regex = new Regex(@"FTS: (\d+) From: (\d+)");
        var match = regex.Match(message);
        var result = new List<int>();
        if (match.Success)
        {
            int finalTS = int.Parse(match.Groups[1].Value);
            int source = int.Parse(match.Groups[2].Value);
            result.Add(finalTS);
            result.Add(source);
            finalTimestamps.Add(result);
        }
        if (finalTimestamps.Count == receivedMessage.Count)
        {
            PrintReadyMessage(finalTimestamps, receivedMessage);
        }
    }

    private void PrintReadyMessage(List<List<int>> finalTimestamps, List<string> receivedMessage)
    {
        var sortedTimestamps = finalTimestamps.OrderBy(sublist => sublist[0]).ThenBy(sublist => sublist[1]).ToList();
        foreach (List<int> i in sortedTimestamps)
        {
            int targetSource = i[1];
            foreach (string j in receivedMessage)
            {
                if (j.Contains($"Middleware {targetSource}"))
                {
                    richTextBox3.AppendText(j + "\n");
                }
            }
        }
        finalTimestamps.Clear();
        receivedMessage.Clear();
    }
}