using System;
using System.IO;
using System.Windows.Forms;
using Microsoft.Extensions.Configuration;

public partial class MainForm : Form
{
    private IConfiguration Configuration;

    public MainForm(IConfiguration configuration)
    {
        InitializeComponent();
        Configuration = configuration;
        LoadConfiguration("Development"); // Esempio di pre-caricamento delle configurazioni di Development
    }

    private void LoadConfiguration(string environment)
    {
        var envConfig = Configuration.GetSection($"Environments:{environment}");

        txtHostname.Text = envConfig["Hostname"];
        txtPort.Text = envConfig["Port"];
        txtQueueManager.Text = envConfig["QueueManager"];
        txtChannel.Text = envConfig["Channel"];
        txtQueueName.Text = envConfig["QueueName"];
        txtUser.Text = envConfig["User"];
        txtPassword.Text = envConfig["Password"];
    }

    private void btnLoadMessage_Click(object sender, EventArgs e)
    {
        using (OpenFileDialog openFileDialog = new OpenFileDialog())
        {
            openFileDialog.InitialDirectory = "c:\\";
            openFileDialog.Filter = "txt files (*.txt)|*.txt|All files (*.*)|*.*";
            openFileDialog.FilterIndex = 2;
            openFileDialog.RestoreDirectory = true;

            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                string filePath = openFileDialog.FileName;
                richTextBoxMessage.Text = File.ReadAllText(filePath);
            }
        }
    }
}
