#include <SoftwareSerial.h>

const byte UP = 48;
const byte STOP = 49;
const byte DOWN = 50;

const byte rxPin = 8;
const byte txPin = 9;
const byte upPin = 10;
const byte stopPin = 11;
const byte downPin = 12;

byte data;

SoftwareSerial softSerial(rxPin, txPin);

void setup()
{
  softSerial.begin(9600);
  initRelay();
}

void loop()
{
  if(softSerial.available())
  {
    do
    {
      data = (byte)softSerial.read();
    }
    while(softSerial.available());
    switch (data)
    {
      case UP:
        trigSignal(upPin);
        break;
      case STOP:
        trigSignal(stopPin);
        break;
      case DOWN:
        trigSignal(downPin);
        break;
    }
  }
}

void initRelay()
{
  pinMode(upPin, OUTPUT);
  pinMode(stopPin, OUTPUT);
  pinMode(downPin, OUTPUT);
  digitalWrite(upPin, LOW);
  digitalWrite(stopPin, LOW);
  digitalWrite(downPin, LOW);
}

void trigSignal(byte pin)
{
  digitalWrite(pin, HIGH);
  delay(10);
  digitalWrite(pin, LOW);
}

